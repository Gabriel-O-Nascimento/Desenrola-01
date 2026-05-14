export const budgetsData = [
  {
    id: 1,
    service: {
      name: "Conserto de vazamento",
      date: "02/04/2026",
      time: "12:03",
      address: "Rua das Flores, 123 - Centro",
    },
    professional: {
      name: "Marcos Antonio",
      initials: "MA",
    },
    labor: [
      {
        id: 1,
        name: "Diagnostico e reparo do vazamento",
        time: "2 horas",
        totalValue: "R$ 120,00",
        hourlyValue: "R$ 60,00/h",
      },
      {
        id: 2,
        name: "Teste de pressao e acabamento",
        time: "1 hora",
        totalValue: "R$ 60,00",
        hourlyValue: "R$ 60,00/h",
      },
    ],
    materials: [
      {
        id: 1,
        name: "Conexao PVC",
        quantity: "2 unidades",
        totalValue: "R$ 24,00",
        unitValue: "R$ 12,00",
      },
      {
        id: 2,
        name: "Fita veda rosca",
        quantity: "1 unidade",
        totalValue: "R$ 10,00",
        unitValue: "R$ 10,00",
      },
    ],
    summary: {
      laborTotal: "R$ 180,00",
      materialsTotal: "R$ 34,00",
      serviceTotal: "R$ 214,00",
    },
    observation:
      "O reparo inclui identificacao do ponto de vazamento, substituicao das conexoes necessarias e teste final para garantir que o problema foi resolvido.",
  },
];
