package com.aura.connectcampus.colleges;

import org.springframework.data.jpa.domain.Specification;

public final class CollegeSpecs {
    private CollegeSpecs() {}

    public static Specification<College> textLike(String q) {
        if (q == null || q.isBlank()) return null;
        String like = "%" + q.toLowerCase() + "%";
        return (root, cq, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), like),
                cb.like(cb.lower(root.get("city")), like),
                cb.like(cb.lower(root.get("state")), like)
        );
    }

    public static Specification<College> cityEq(String city) {
        if (city == null || city.isBlank()) return null;
        return (root, cq, cb) -> cb.equal(cb.lower(root.get("city")), city.toLowerCase());
    }

    public static Specification<College> stateEq(String state) {
        if (state == null || state.isBlank()) return null;
        return (root, cq, cb) -> cb.equal(cb.lower(root.get("state")), state.toLowerCase());
    }

    public static Specification<College> minFees(Integer min) {
        if (min == null) return null;
        return (root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("fees"), min);
    }

    public static Specification<College> maxFees(Integer max) {
        if (max == null) return null;
        return (root, cq, cb) -> cb.lessThanOrEqualTo(root.get("fees"), max);
    }

    public static Specification<College> all(String q, String city, String state, Integer minFees, Integer maxFees) {
        Specification<College> spec = Specification.where(textLike(q));
        if (city != null) spec = spec.and(cityEq(city));
        if (state != null) spec = spec.and(stateEq(state));
        if (minFees != null) spec = spec.and(minFees(minFees));
        if (maxFees != null) spec = spec.and(maxFees(maxFees));
        return spec;
    }
}
