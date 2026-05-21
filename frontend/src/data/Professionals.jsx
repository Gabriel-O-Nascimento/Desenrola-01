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
        description: "Reparos, construcoes e pequenas reformas residenciais.",
        icon: Hammer,
        route: "/servicos/pedreiro",
      },
      {
        id: 2,
        name: "Pintor",
        description: "Pintura de paredes, acabamentos e retoques em geral.",
        icon: PaintRoller,
        route: "/servicos/pintor",
      },
      {
        id: 3,
        name: "Eletricista",
        description: "Instalacoes, manutencoes e reparos eletricos.",
        icon: Zap,
        route: "/servicos/eletricista",
      },
      {
        id: 4,
        name: "Encanador",
        description: "Reparo e instalacao de tubulacoes, torneiras e sistemas hidraulicos.",
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
        description: "Orientacao profissional para processos e negocios.",
        icon: Briefcase,
        route: "/servicos/consultor",
      },
      {
        id: 6,
        name: "Designer",
        description: "Criacao visual, identidade e materiais digitais.",
        icon: PenTool,
        route: "/servicos/designer",
      },
      {
        id: 7,
        name: "Tecnico TI",
        description: "Suporte tecnico, manutencao e configuracao de sistemas.",
        icon: MonitorCog,
        route: "/servicos/tecnico-ti",
      },
      {
        id: 8,
        name: "Manutencao",
        description: "Servicos gerais de manutencao para empresas.",
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
        description: "Atendimento para relaxamento e bem-estar corporal.",
        icon: Sparkles,
        route: "/servicos/massagista",
      },
      {
        id: 10,
        name: "Manicure",
        description: "Cuidados com unhas, esmalteria e acabamento.",
        icon: Hand,
        route: "/servicos/manicure",
      },
      {
        id: 11,
        name: "Cabeleireiro",
        description: "Cortes, tratamentos e finalizacao de cabelo.",
        icon: Scissors,
        route: "/servicos/cabeleireiro",
      },
      {
        id: 12,
        name: "Barbeiro",
        description: "Corte masculino, barba e acabamento.",
        icon: Scissors,
        route: "/servicos/barbeiro",
      },
    ],
  },
];
