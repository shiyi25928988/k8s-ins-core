package yi.shi.ssh.k8s;

public enum KubernetesActionType {
    INSTALL_MASTER_NODE_1_21_1(false),

    JOIN_IN_1_21_1(true);

    boolean isJoin;

    KubernetesActionType(boolean isJoin) {
        this.isJoin = isJoin;
    }

    public static KubernetesActionType getActionTypeByVersion(boolean isJoin) {
        for (KubernetesActionType actionType : KubernetesActionType.values()) {
            if (actionType.isJoin == isJoin) {
                return actionType;
            }
        }
        return null;
    }
}
