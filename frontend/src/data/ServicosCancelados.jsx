export const servicosCancelados = [
  {
    id: 5,
    professionalId: 2,
    serviceId: 2,
    categoryId: "pintura",
    serviceCode: "#SV-00125",
    status: "cancelado",
    serviceType: "Pintura de parede",
    category: "Pintura",
    clientName: "João Silva",
    professionalName: "Maria Santos",
    requestDate: "08/12/2025",
    cancellationDate: "10/12/2025",
    address: "Av. Central, 321 - Bairro Jardim",
    estimatedValue: "R$ 450.00",
    description:
      "Pintura completa de sala e quarto, incluindo preparação da parede, massa corrida e duas demãos de tinta acrílica premium na cor branco gelo.",
    cancellationReason:
      "O profissional cancelou este serviço devido a um imprevisto pessoal que impossibilitou o atendimento na data agendada. Pedimos desculpas pelo transtorno e convidamos você a solicitar novamente o serviço.",
    history: [
      { id: 1, label: "Serviço Solicitado", date: "08/12/2025", status: "success" },
      { id: 2, label: "Em Análise", date: "08/12/2025", status: "info" },
      { id: 3, label: "Cancelado", date: "10/12/2025", status: "danger" },
    ],
  },
];
