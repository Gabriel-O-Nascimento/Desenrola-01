import {
  Hammer,
  PaintRoller,
  Wrench,
  Briefcase,
  PenTool,
  MonitorCog,
  Scissors,
  Hand,
  Sparkles,
  Zap,
} from "lucide-react";

/* Lista de servicos exibida na Home. */
export const professionalsLists = [
  {
    title: "Casa e Construcao",
    items: [
      {
        id: 1,
        name: "Pedreiro",
        icon: Hammer,
        route: "/servicos/pedreiro",
      },
      {
        id: 2,
        name: "Pintor",
        icon: PaintRoller,
        route: "/servicos/pintor",
      },
      {
        id: 3,
        name: "Eletricista",
        icon: Zap,
        route: "/servicos/eletricista",
      },
      {
        id: 4,
        name: "Encanador",
        icon: Wrench,
        route: "/servicos/encanador",
      },
    ],
  },

  {
    title: "Empresarial",
    items: [
      {
        id: 5,
        name: "Consultor",
        icon: Briefcase,
        route: "/servicos/consultor",
      },
      {
        id: 6,
        name: "Designer",
        icon: PenTool,
        route: "/servicos/designer",
      },
      {
        id: 7,
        name: "Tecnico TI",
        icon: MonitorCog,
        route: "/servicos/tecnico-ti",
      },
      {
        id: 8,
        name: "Manutencao",
        icon: Wrench,
        route: "/servicos/manutencao",
      },
    ],
  },

  {
    title: "Saude e Beleza",
    items: [
      {
        id: 9,
        name: "Massagista",
        icon: Sparkles,
        route: "/servicos/massagista",
      },
      {
        id: 10,
        name: "Manicure",
        icon: Hand,
        route: "/servicos/manicure",
      },
      {
        id: 11,
        name: "Cabeleireiro",
        icon: Scissors,
        route: "/servicos/cabeleireiro",
      },
      {
        id: 12,
        name: "Barbeiro",
        icon: Scissors,
        route: "/servicos/barbeiro",
      },
    ],
  },
];