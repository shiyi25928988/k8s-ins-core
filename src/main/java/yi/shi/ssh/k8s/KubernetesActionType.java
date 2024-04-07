package yi.shi.ssh.k8s;

public enum KubernetesActionType {
    INSTALL_MASTER_NODE_1_21_1("1.21.1", false),

    INSTALL_MASTER_NODE_1_28_2("1.28.2", false),
    JOIN_IN_1_21_1("1.21.1", true),

    JOIN_IN_1_28_2("1.28.2", true),;

    String version;

    boolean isJoin;

    KubernetesActionType(String version, boolean isJoin) {
        this.version = version;
        this.isJoin = isJoin;
    }

    public static KubernetesActionType getActionTypeByVersion(String version, boolean isJoin) {
        for (KubernetesActionType actionType : KubernetesActionType.values()) {
            if (actionType.getVersion().equals(version) && actionType.isJoin == isJoin) {
                return actionType;
            }
        }
        return null;
    }

    public String getVersion() {
        return version;
    }
}
