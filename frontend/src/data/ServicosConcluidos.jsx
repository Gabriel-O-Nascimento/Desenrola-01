import { Image } from "lucide-react";

export const servicosConcluidos = [
  {
    id: 4,
    professionalId: 1,
    professionalName: "João Silva",
    professionalInitials: "JS",
    professionalCategory: "Eletricista Profissional",
    professionalRating: 5.0,
    title: "Manutenção elétrica",
    category: "Elétrica",
    startDate: "10/12/2025",
    conclusionDate: "10/12/2025",
    dateTime: "15/12/2025 às 14:00",
    address: "Rua das Flores, 123 - Centro, São Paulo - SP",
    description:
      "Realizada revisão completa do quadro elétrico, substituição de disjuntores antigos, instalação de novos circuitos para ar-condicionado e verificação de aterramento. Todos os componentes foram testados e certificados conforme normas da ABNT.",
    photos: [
      { id: 1, label: "Antes", icon: Image },
      { id: 2, label: "Durante", icon: Image },
      { id: 3, label: "Depois", icon: Image },
      { id: 4, label: "Detalhe", icon: Image },
    ],
    financialSummary: {
      labor: "R$ 260,00",
      materials: "R$ 202,00",
      discount: "- R$ 50,00",
      totalPaid: "R$ 412,00",
    },
    paymentMethod: {
      type: "Pix",
      status: "Pagamento confirmado",
    },
    finalNotes:
      "Serviço executado dentro do prazo combinado. Recomendamos manutenção preventiva a cada 6 meses para garantir a segurança da instalação elétrica.",
  },
];
