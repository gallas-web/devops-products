# 🚀 Configuration Kubernetes - Product CRUD

Ce dossier contient tous les manifests Kubernetes pour déployer l'application Product CRUD avec ArgoCD.

## 📋 Architecture

```
Namespace: product-crud
├── Backend
│   ├── Deployment (2 replicas)
│   ├── Service (ClusterIP:8080)
│   └── Health checks + Resource limits
└── Frontend
    ├── Deployment (2 replicas)
    ├── Service (LoadBalancer:80)
    └── Health checks + Resource limits
```

## ⚙️ Configuration requise

Avant de deployer, vous devez :

### 1. **Remplacer {{AWS_ACCOUNT_ID}}**

Trouvez votre AWS Account ID :
```bash
aws sts get-caller-identity --query Account --output text
```

Puis remplacez dans les fichiers :
- `kubernetes/backend/deployment.yaml` ligne ~27
- `kubernetes/frontend/deployment.yaml` ligne ~47

Exemple: `123456789012.dkr.ecr.us-east-1.amazonaws.com/...`

### 2. **Créer le secret ECR dans Kubernetes**

Le cluster a besoin d'authentifier ECR pour tirer les images.

```bash
# Créer docker config pour ECR
TOKEN=$(aws ecr get-authorization-token --region us-east-1 --output text --query authorizationData[].authorizationToken)

kubectl create secret docker-registry ecr-secret \
  --docker-server=ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com \
  --docker-username=AWS \
  --docker-password=$TOKEN \
  --docker-email=noreply@example.com \
  -n product-crud
```

### 3. **Valider les manifests**

```bash
# Vérifier la syntaxe
kubectl apply -f kubernetes/ --dry-run=client

# Ou avec kustomize
kustomize build kubernetes/
```

## 🔄 Flux GitOps avec ArgoCD

1. Vous committez les manifests YAML dans Git
2. ArgoCD poll ce repo toutes les 3 minutes
3. ArgoCD détecte les changements
4. ArgoCD applique les manifests au cluster
5. Kubernetes crée/met à jour les pods

## 📝 Fichiers inclus

| Fichier | Description |
|---------|-------------|
| `namespace.yaml` | Crée le namespace `product-crud` |
| `backend/deployment.yaml` | Déploiement du backend Spring Boot |
| `backend/service.yaml` | Service ClusterIP pour le backend |
| `frontend/deployment.yaml` | Déploiement du frontend Angular |
| `frontend/service.yaml` | Service LoadBalancer pour le frontend |
| `kustomization.yaml` | Agrégation de tous les manifests |

## 🎯 Points clés

✅ **Réplicas**: 2 par défaut (tolérance aux pannes)  
✅ **Health checks**: Liveness + Readiness probes  
✅ **Resource limits**: Évite la famine CPU/mémoire  
✅ **Pod affinity**: Distribue les pods sur différents nœuds  
✅ **Rolling updates**: Zéro downtime sur les deployments  

## 📊 Variables d'environnement

### Backend
- `ENVIRONMENT` → "production"
- `JAVA_OPTS` → Tuning JVM

### Frontend
- `ENVIRONMENT` → "production"
- `API_URL` → URL du backend (`http://product-crud-backend:8080/api`)

## 🔗 Prochaines étapes

1. Remplacer `{{AWS_ACCOUNT_ID}}`
2. Créer le cluster EKS
3. Installer ArgoCD
4. Créer l'Application ArgoCD
5. Déclencher le sync automatique

## 📚 Référence

- [Kubernetes Deployment](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/)
- [Kustomize](https://kustomize.io/)
- [ArgoCD](https://argoproj.github.io/argo-cd/)
