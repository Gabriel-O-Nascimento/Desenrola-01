#!/bin/bash
# Verifica se o repositório local está atualizado com o remoto (GitHub)

echo "🔄 Verificando atualizações do repositório remoto..."

# Busca as referências mais recentes do remoto sem alterar nada local
git fetch origin 2>/dev/null

if [ $? -ne 0 ]; then
  echo "⚠️ Não foi possível conectar ao repositório remoto. Verifique sua conexão."
  exit 1
fi

BRANCH=$(git branch --show-current 2>/dev/null)

if [ -z "$BRANCH" ]; then
  echo "⚠️ Não foi possível identificar a branch atual."
  exit 1
fi

LOCAL=$(git rev-parse "$BRANCH" 2>/dev/null)
REMOTE=$(git rev-parse "origin/$BRANCH" 2>/dev/null)

if [ -z "$REMOTE" ]; then
  echo "ℹ️ Branch '$BRANCH' não existe no remoto ainda."
  exit 0
fi

if [ "$LOCAL" = "$REMOTE" ]; then
  echo "✅ Repositório atualizado. Branch '$BRANCH' está em dia com o remoto."
else
  BEHIND=$(git rev-list --count "$BRANCH".."origin/$BRANCH" 2>/dev/null)
  AHEAD=$(git rev-list --count "origin/$BRANCH".."$BRANCH" 2>/dev/null)

  if [ "$BEHIND" -gt 0 ]; then
    echo "🚨 ATENÇÃO: Você está $BEHIND commit(s) ATRÁS do remoto na branch '$BRANCH'."
    echo "   Execute 'git pull' antes de continuar para evitar conflitos."
    echo ""
    echo "   Últimos commits no remoto que você não tem:"
    git log --oneline "$BRANCH".."origin/$BRANCH" | head -5
  fi

  if [ "$AHEAD" -gt 0 ]; then
    echo "📤 Você tem $AHEAD commit(s) local(is) que ainda não foram enviados (git push)."
  fi
fi
