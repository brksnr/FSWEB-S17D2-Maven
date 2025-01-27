package com.workintech.s17d2.model;

public enum Experience {
    JUNIOR, MID, SENIOR;

    public static Experience fromString(String experience) {
        try {
            return Experience.valueOf(experience.toUpperCase()); // Enum'daki deneyim değerini küçük harfe dönüştürüp eşleştiriyoruz
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid experience level: " + experience);
        }
    }
    Experience() {
    }

    @Override
    public String toString() {
        return "Experience{}";
    }
}




