import Card from "./Card";
import "../../styles/global.css";

/* Lista responsiva que sempre exibe 4 cards na mesma linha. */
export default function ListaDeCards({ list }) {
  /* Garante que apenas os 4 primeiros itens sejam exibidos na linha. */
  const visibleItems = list.items.slice(0, 4);

  return (
    <section className="lista-de-cards" aria-labelledby={`lista-${list.title}`}>
      {/* Titulo vindo do campo title e alinhado ao primeiro card. */}
      <h3 className="lista-de-cards__title" id={`lista-${list.title}`}>
        {list.title}
      </h3>

      {/* Grade fluida com exatamente 4 colunas, sem quebra e sem scroll horizontal. */}
      <div className="lista-de-cards__grid">
        {visibleItems.map((item) => (
          <Card key={item.id} item={item} />
        ))}
      </div>
    </section>
  );
}
