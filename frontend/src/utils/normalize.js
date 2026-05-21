import { Package } from "lucide-react";
import { resolveIcon } from "./iconMap";

export function normalizeSection(section) {
  return {
    ...section,
    items: (section.items || []).map((item) => ({
      ...item,
      icon: resolveIcon(item.iconName, Package),
    })),
  };
}

export function normalizeProfessional(professional) {
  return {
    ...professional,
    works: (professional.works || []).map((work) => ({
      ...work,
      icon: resolveIcon(work.iconName, Package),
    })),
    reviews: (professional.reviews || []).map((review) => ({
      ...review,
      imageIcon: review.imageIconName ? resolveIcon(review.imageIconName, Package) : null,
    })),
  };
}

export function normalizeServiceList(services) {
  return services.map((service) => ({
    ...service,
    icon: resolveIcon(service.iconName, Package),
  }));
}

export function normalizeTracking(tracking) {
  return {
    ...tracking,
    photos: (tracking.photos || []).map((photo) => ({
      ...photo,
      icon: resolveIcon(photo.iconName, Package),
    })),
  };
}

export function normalizeCompletedService(service) {
  return {
    ...service,
    photos: (service.photos || []).map((photo) => ({
      ...photo,
      icon: resolveIcon(photo.iconName, Package),
    })),
  };
}
