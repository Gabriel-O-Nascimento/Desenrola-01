import { Hammer, PaintRoller, Wrench, Zap } from "lucide-react";

export const professionals = [
  {
    id: 1,
    name: "Joao Silva",
    initials: "JS",
    category: "Eletricista residencial",
    rating: 4.9,
  },
  {
    id: 2,
    name: "Maria Santos",
    initials: "MS",
    category: "Pintora profissional",
    rating: 4.8,
  },
  {
    id: 3,
    name: "Carlos Mendes",
    initials: "CM",
    category: "Encanador",
    rating: 4.7,
  },
];

export const services = [
  {
    id: 1,
    name: "Eletricista",
    description: "Instalacoes, manutencoes e reparos eletricos.",
    icon: Zap,
  },
  {
    id: 2,
    name: "Pintura",
    description: "Pintura residencial, comercial e acabamentos.",
    icon: PaintRoller,
  },
  {
    id: 3,
    name: "Encanamento",
    description: "Consertos, vazamentos e instalacoes hidraulicas.",
    icon: Wrench,
  },
  {
    id: 4,
    name: "Obras e reformas",
    description: "Pequenos reparos, construcao e reforma geral.",
    icon: Hammer,
  },
];
