package com.example.cinema.util;

/**
 * Theater card images served from {@code src/main/resources/static/images}.
 */
public final class TheaterImages {

    public static final String[] PATHS = {
            "/images/ChatGPT Image May 20, 2026, 08_36_55 PM.png",
            "/images/ChatGPT Image May 20, 2026, 08_40_57 PM.png",
            "/images/ChatGPT Image May 20, 2026, 08_42_43 PM.png",
            "/images/ChatGPT Image May 20, 2026, 10_44_59 AM.png",
            "/images/ChatGPT Image May 20, 2026, 10_51_00 AM.png",
            "/images/ChatGPT Image May 20, 2026, 10_54_09 AM.png",
            "/images/Galaxy-Theatres-DFX-Auditorium-1024x560.jpg",
            "/images/Gemini_Generated_Image_ihpj8iihpj8iihpj.png",
            "/images/pngtree-empty-movie-theater-with-rows-of-vacant-red-seats-leading-to-image_19255538.webp",
            "/images/modern-home-theater-with-plush-seating-and-ambient-lighting-free-photo.jfif"
    };

    private TheaterImages() {
    }

    public static String pathForIndex(int index) {
        if (PATHS.length == 0) {
            return null;
        }
        return PATHS[Math.floorMod(index, PATHS.length)];
    }
}
