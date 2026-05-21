import {
  BrickWall,
  Building2,
  Camera,
  Droplets,
  Hammer,
  Home,
  Layers,
  Lightbulb,
  Paintbrush,
  Plug,
  ShowerHead,
  Wrench,
} from "lucide-react";

export const cadastrosProfissionais = [
  {
    id: 1,
    name: "João Silva",
    initials: "JS",
    profession: "Eletricista residencial",
    categoryId: "eletrica",
    serviceId: 3,
    verified: true,
    rating: 4.9,
    servicesCount: 127,
    distance: "2.3 km",
    responseTime: "1h",
    about:
      "Profissional com 5 anos de experiência em instalações e manutenção elétrica residencial. Especialista em troca de tomadas, iluminação, quadros elétricos e pequenos reparos com atendimento ágil.",
    works: [
      { id: 1, icon: Home, title: "Instalação residencial" },
      { id: 2, icon: Plug, title: "Troca de tomadas" },
      { id: 3, icon: Lightbulb, title: "Iluminação" },
    ],
    reviews: [
      {
        id: 1,
        clientName: "Maria Santos",
        date: "15/11/2025",
        rating: 5,
        comment:
          "Excelente profissional! Resolveu o problema rapidamente e deixou tudo limpo. Super recomendo!",
      },
      {
        id: 2,
        clientName: "Carlos Mendes",
        date: "10/11/2025",
        rating: 4,
        comment:
          "Muito bom! Pontual e eficiente. Único ponto é que demorou um pouco mais que o esperado.",
        imageIcon: Camera,
      },
      {
        id: 3,
        clientName: "Ana Paula",
        date: "05/11/2025",
        rating: 5,
        comment:
          "Perfeito! Preço justo e trabalho de qualidade. Já salvei o contato.",
      },
    ],
  },
  {
    id: 2,
    name: "Maria Santos",
    initials: "MS",
    profession: "Pintora profissional",
    categoryId: "pintura",
    serviceId: 2,
    verified: true,
    rating: 4.8,
    servicesCount: 89,
    distance: "3.1 km",
    responseTime: "45min",
    about:
      "Pintora profissional especializada em pintura residencial, acabamento fino, textura e revitalização de ambientes. Atua com organização, proteção dos móveis e limpeza ao final do serviço.",
    works: [
      { id: 1, icon: Paintbrush, title: "Pintura interna" },
      { id: 2, icon: Building2, title: "Pintura comercial" },
      { id: 3, icon: Layers, title: "Texturas" },
    ],
    reviews: [
      {
        id: 1,
        clientName: "Juliana Ferreira",
        date: "12/11/2025",
        rating: 5,
        comment:
          "O acabamento ficou impecável. Profissional muito cuidadosa e organizada.",
      },
      {
        id: 2,
        clientName: "Ricardo Lima",
        date: "08/11/2025",
        rating: 4,
        comment:
          "Serviço muito bom e preço justo. Recomendo para pintura residencial.",
      },
    ],
  },
  {
    id: 3,
    name: "Carlos Mendes",
    initials: "CM",
    profession: "Encanador",
    categoryId: "hidraulica",
    serviceId: 4,
    verified: true,
    rating: 4.7,
    servicesCount: 104,
    distance: "1.8 km",
    responseTime: "30min",
    about:
      "Encanador com experiência em manutenção hidráulica residencial e comercial. Realiza reparos de vazamentos, instalação de torneiras, chuveiros e troca de sistemas hidráulicos.",
    works: [
      { id: 1, icon: ShowerHead, title: "Reparo de chuveiro" },
      { id: 2, icon: Wrench, title: "Manutenção hidráulica" },
      { id: 3, icon: Droplets, title: "Troca de torneira" },
    ],
    reviews: [
      {
        id: 1,
        clientName: "Pedro Costa",
        date: "14/11/2025",
        rating: 5,
        comment:
          "Atendimento rápido e resolveu o vazamento no mesmo dia.",
      },
      {
        id: 2,
        clientName: "Marcos Antonio",
        date: "06/11/2025",
        rating: 4,
        comment:
          "Bom profissional, explicou o problema e cobrou um valor justo.",
        imageIcon: Camera,
      },
    ],
  },
  {
    id: 4,
    name: "Pedro Costa",
    initials: "PC",
    profession: "Pedreiro",
    categoryId: "construcao",
    serviceId: 1,
    verified: false,
    rating: 4.6,
    servicesCount: 63,
    distance: "4.2 km",
    responseTime: "2h",
    about:
      "Pedreiro com experiência em pequenos reparos, alvenaria, pisos e reformas residenciais. Atendimento sob orçamento e acompanhamento do início ao fim.",
    works: [
      { id: 1, icon: BrickWall, title: "Alvenaria" },
      { id: 2, icon: Home, title: "Reformas" },
      { id: 3, icon: Hammer, title: "Reparos gerais" },
    ],
    reviews: [
      {
        id: 1,
        clientName: "Joana Lima",
        date: "09/11/2025",
        rating: 5,
        comment: "Trabalho bem feito e entrega dentro do combinado.",
      },
    ],
  },
];

export const cadastroProfissional = cadastrosProfissionais[0];
