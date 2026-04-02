package org.global.housing.service;

import org.global.housing.dto.PisosItem;
import org.global.housing.dto.PisosListing;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class PisosSearchService {

    private static final Logger logger = LoggerFactory.getLogger(PisosSearchService.class);

    // Timeout en ms
    private static final int TIMEOUT = 10000;
    // Pause entre páginas para no bombardear la web (en ms)
    private static final long PAGE_DELAY_MS = 400;

    public List<PisosListing> search(String municipio, String cssSelector, Integer priceFrom, Integer priceTo) throws Exception {
        if (municipio == null || municipio.trim().isEmpty()) {
            throw new IllegalArgumentException("municipio is required");
        }

        String slug = municipio.trim().toLowerCase().replace(' ', '-');
        String priceSegment = "";
        if (priceFrom != null) {
            priceSegment += "desde-" + priceFrom + "/";
        }
        if (priceTo != null) {
            priceSegment += "hasta-" + priceTo + "/";
        }

        String basePath = "https://www.pisos.com/venta/pisos-" + URLEncoder.encode(slug, StandardCharsets.UTF_8) + "/" + priceSegment;

        logger.info("Buscando paginada en {} selector={} priceFrom={} priceTo={}", basePath, cssSelector, priceFrom, priceTo);

        List<PisosListing> results = new ArrayList<>();
        int page = 1;
        while (true) {
            String url = (page == 1) ? basePath : basePath + page + "/";
            logger.info("Conectando a página {} -> {}", page, url);

            Connection connection = Jsoup.connect(url)
                    .timeout(TIMEOUT)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0 Safari/537.36")
                    .referrer("https://www.google.com")
                    .ignoreHttpErrors(true)
                    .followRedirects(true);

            Document doc = null;
            try {
                doc = connection.get();
            } catch (Exception e) {
                logger.warn("Error al obtener página {}: {}", url, e.getMessage());
                // Si no se puede obtener la página, salimos y devolvemos lo acumulado
                break;
            }

            int status = connection.response().statusCode();
            if (status == 404) {
                logger.info("Página {} devolvió 404 -> fin de paginación", url);
                break;
            }
            if (status != 200) {
                logger.warn("Página {} devolvió status {} -> deteniendo paginación", url, status);
                break;
            }

            // Detectar página sin resultados explícita
            Elements noResults = doc.select(".no-results");
            if (noResults != null && !noResults.isEmpty()) {
                logger.info("Página {} contiene .no-results -> fin de paginación", url);
                break;
            }

            // Detectar bloqueo por JS / captcha (fragmentos conocidos)
            String bodyText = doc.body() != null ? doc.body().text() : "";
            String html = doc.html();
            if (bodyText.contains("Please enable JS") || html.contains("captcha-delivery") || html.contains("Please enable JS and disable any ad blocker")) {
                logger.warn("Página {} parece un bloqueo JS/captcha -> deteniendo paginación (content-snippet: {})", url, bodyText.length() > 120 ? bodyText.substring(0, 120) + "..." : bodyText);
                break;
            }

            // seleccionar elementos (permitir selector personalizado)
            Elements elements;
            if (cssSelector == null || cssSelector.trim().isEmpty()) {
                elements = doc.select(".ad-preview");
            } else {
                elements = doc.select(cssSelector);
            }

            // Si existe el marcador que separa sugerencias, truncamos los listings que vienen después
            Element marker = doc.selectFirst("p.grid__subtitle");
            if (marker != null) {
                // buscamos en el padre del marcador los ad-preview que aparecen antes del marcador
                Element parent = marker.parent();
                Elements before = new Elements();
                for (Element child : parent.children()) {
                    if (child.equals(marker)) {
                        break;
                    }
                    // si el propio child es un ad-preview lo añadimos
                    if (child.hasClass("ad-preview")) {
                        before.add(child);
                    } else {
                        // también recogemos .ad-preview anidados dentro del child
                        before.addAll(child.select(".ad-preview"));
                    }
                }
                if (!before.isEmpty()) {
                    logger.info("Marcador 'grid__subtitle' encontrado: truncando {} listings a {} (antes del marcador)", elements.size(), before.size());
                    elements = before;
                } else {
                    logger.info("Marcador 'grid__subtitle' encontrado pero no se hallaron ad-preview antes del marcador; no se aplica truncado");
                }
            }

            logger.info("Página {}: encontrados {} items", page, elements.size());

            // Si no hay elementos en una página válida, consideramos terminar para evitar bucles infinitos
            if (elements.isEmpty()) {
                logger.info("Página {} no contiene listings. Deteniendo paginación.", page);
                break;
            }

            for (Element el : elements) {
                PisosListing p = new PisosListing();

                String favoriteId = el.select(".favorite.js-addFav").attr("data-id");
                if (favoriteId == null || favoriteId.isEmpty()) {
                    favoriteId = el.attr("id");
                }
                p.setFavoriteId(favoriteId);

                String link = el.select(".ad-preview__title").stream().findFirst().map(a -> a.absUrl("href")).orElse(null);
                if (link == null || link.isEmpty()) {
                    link = el.attr("data-lnk-href");
                    if (link != null && !link.startsWith("http")) {
                        link = "https://www.pisos.com" + link;
                    }
                }
                p.setLink(link);

                String price = el.select(".ad-preview__price").stream().findFirst().map(Element::text).orElse(null);
                p.setPrice(price != null ? price.trim() : null);

                String priceDrop = el.select(".ad-preview__drop").stream().findFirst().map(Element::text).orElse(null);
                p.setPriceDrop(priceDrop != null ? priceDrop.trim() : null);

                String title = el.select(".ad-preview__title").text();
                p.setTitle(title != null && !title.isEmpty() ? title.trim() : null);
                String subtitle = el.select(".ad-preview__subtitle").text();
                p.setSubtitle(subtitle != null && !subtitle.isEmpty() ? subtitle.trim() : null);

                Elements chars = el.select(".ad-preview__char");
                if (chars != null && !chars.isEmpty()) {
                    if (chars.size() > 0) p.setBedrooms(chars.get(0).text());
                    if (chars.size() > 1) p.setBathrooms(chars.get(1).text());
                    if (chars.size() > 2) p.setArea(chars.get(2).text());
                }

                String description = el.select(".ad-preview__description").text();
                p.setDescription(description != null && !description.isEmpty() ? description.trim() : null);

                String photosCounter = el.select(".js-photosCounter").text();
                try {
                    if (photosCounter != null && !photosCounter.isEmpty()) {
                        p.setPhotosCount(Integer.parseInt(photosCounter.trim()));
                    }
                } catch (NumberFormatException ignore) {}

                boolean hasVideo = el.select(".js-photosCounter, .multimedia-counter__video").stream().anyMatch(e -> !e.text().isEmpty() || e.hasClass("multimedia-counter__video"));
                p.setHasVideo(hasVideo);

                String image = el.select("img.carousel__main-photo, img.carousel__main-photo--mosaic, img[data-src]").stream().findFirst().map(img -> img.attr("data-src")).orElse(null);
                if (image == null || image.isEmpty()) {
                    image = el.select("script[type=application/ld+json]").stream().findFirst().map(Element::data).map(d -> {
                        int i = d.indexOf("contentUrl");
                        if (i >= 0) {
                            int s = d.indexOf(':', i)+1;
                            int q1 = d.indexOf('"', s);
                            int q2 = d.indexOf('"', q1+1);
                            if (q1>=0 && q2>q1) return d.substring(q1+1, q2);
                        }
                        int j = d.indexOf("\"image\":");
                        if (j>=0) {
                            int q1 = d.indexOf('"', j+9);
                            int q2 = d.indexOf('"', q1+1);
                            if (q1>=0 && q2>q1) return d.substring(q1+1, q2);
                        }
                        return null;
                    }).orElse(null);
                }
                p.setImage(image);

                // Extraer todas las imágenes del carrusel (data-src o src) y deduplicar
                Elements carouselImgs = el.select(".carousel img, .carousel__container img, img.carousel__main-photo, img.carousel__main-photo--mosaic, img[data-src], img[src]");
                List<String> images = new ArrayList<>();
                for (Element imgEl : carouselImgs) {
                    String src = imgEl.hasAttr("data-src") ? imgEl.attr("abs:data-src") : imgEl.hasAttr("src") ? imgEl.attr("abs:src") : null;
                    if (src != null && !src.isEmpty() && !images.contains(src)) {
                        images.add(src);
                    }
                }
                if (!images.isEmpty()) {
                    p.setImages(images);
                    // If we didn't set main image earlier, use first of images
                    if ((p.getImage() == null || p.getImage().isEmpty()) && !images.isEmpty()) {
                        p.setImage(images.get(0));
                    }
                }

                String agencyLogo = el.select(".ad-preview__logo img").stream().findFirst().map(img -> img.attr("data-src")).orElse(null);
                p.setAgencyLogo(agencyLogo);

                el.select("script[type=application/ld+json]").forEach(s -> {
                    String d = s.data();
                    if (d.contains("GeoCoordinates") || d.contains("geo")) {
                        try {
                            int latIdx = d.indexOf("\"latitude\"");
                            if (latIdx >= 0) {
                                int q1 = d.indexOf('"', latIdx+11);
                                int q2 = d.indexOf('"', q1+1);
                                String lat = d.substring(q1+1, q2);
                                p.setGeoLatitude(lat);
                            }
                            int lonIdx = d.indexOf("\"longitude\"");
                            if (lonIdx >= 0) {
                                int q1 = d.indexOf('"', lonIdx+12);
                                int q2 = d.indexOf('"', q1+1);
                                String lon = d.substring(q1+1, q2);
                                p.setGeoLongitude(lon);
                            }
                            int idIdx = d.indexOf("\"@id\"");
                            if (idIdx>=0) {
                                int q1 = d.indexOf('"', idIdx+5);
                                int q2 = d.indexOf('"', q1+1);
                                if (q1>=0 && q2>q1) p.setId(d.substring(q1+1, q2));
                            }
                        } catch (Exception ignore) {}
                    }
                });

                String dataAdPrice = el.select(".contact-box").attr("data-ad-price");
                p.setDataAdPrice(dataAdPrice != null && !dataAdPrice.isEmpty() ? dataAdPrice : null);

                if (p.getId() == null || p.getId().isEmpty()) {
                    p.setId(p.getFavoriteId());
                }

                results.add(p);
            }

            // respeto un pequeño delay antes de la siguiente página
            try {
                Thread.sleep(PAGE_DELAY_MS);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
                break;
            }

            page++;
        }

        logger.info("Paginación finalizada. Total items: {} para municipio={}", results.size(), municipio);
        return results;
    }
}

