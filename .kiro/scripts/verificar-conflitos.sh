#!/bin/bash
# Verifica se há conflitos de merge no repositório após modificações

echo "🔍 Verificando conflitos no repositório..."

# Busca atualizações do remoto
git fetch origin 2>/dev/null

BRANCH=$(git branch --show-current 2>/dev/null)

if [ -z "$BRANCH" ]; then
  echo "⚠️ Não foi possível identificar a branch atual."
  exit 1
fi

# Verifica se existem marcadores de conflito em arquivos do projeto
CONFLITOS=$(grep -rl "<<<<<<< " --include="*.ts" --include="*.tsx" --include="*.js" --include="*.jsx" --include="*.json" --include="*.css" --include="*.html" --include="*.md" --include="*.java" --include="*.py" src/ 2>/dev/null)

if [ -n "$CONFLITOS" ]; then
  echo "🚨 CONFLITOS ENCONTRADOS nos seguintes arquivos:"
  echo "$CONFLITOS"
  echo ""
  echo "Resolva os conflitos antes de continuar."
  exit 1
fi

# Tenta um merge dry-run pra ver se há conflitos pendentes com o remoto
REMOTE_REF="origin/$BRANCH"
REMOTE_EXISTS=$(git rev-parse --verify "$REMOTE_REF" 2>/dev/null)

if [ -n "$REMOTE_EXISTS" ]; then
  MERGE_TEST=$(git merge-tree $(git merge-base HEAD "$REMOTE_REF") HEAD "$REMOTE_REF" 2>/dev/null | grep "<<<<<<< ")

  if [ -n "$MERGE_TEST" ]; then
    echo "⚠️ ATENÇÃO: Existem conflitos potenciais entre sua branch local e o remoto."
    echo "   Faça 'git pull' e resolva os conflitos antes de continuar."
    echo ""
    echo "   Arquivos que podem conflitar:"
    git merge-tree $(git merge-base HEAD "$REMOTE_REF") HEAD "$REMOTE_REF" 2>/dev/null | grep "changed in both" | head -10
    exit 1
  fi
fi

echo "✅ Nenhum conflito encontrado na branch '$BRANCH'."
