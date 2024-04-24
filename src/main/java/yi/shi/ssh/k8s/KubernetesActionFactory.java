package yi.shi.ssh.k8s;

import yi.shi.ssh.actions.AbstractAction;
import yi.shi.ssh.actions.NullAction;
import yi.shi.ssh.k8s.actions.CompleteAction;
import yi.shi.ssh.k8s.actions.config.ConfigHostNameAction;
import yi.shi.ssh.k8s.actions.config.ConfigMasterNode_1_21_1_Action;
import yi.shi.ssh.k8s.actions.config.ConfigMasterNode_1_28_2_Action;
import yi.shi.ssh.k8s.actions.env.*;
import yi.shi.ssh.k8s.actions.install.*;
import yi.shi.ssh.k8s.actions.join.JoinAsNodeAction;
import yi.shi.ssh.k8s.actions.join.JoinAsNode_1_28_2_Action;
import yi.shi.ssh.shell.SshContext;

public class KubernetesActionFactory {

    public static AbstractAction getAction(boolean isJoin, SshContext sshContext, String...cmds){

        KubernetesActionType actionTypeEnum = KubernetesActionType.getActionTypeByVersion(isJoin);
        if(actionTypeEnum == null){
            return new NullAction();
        }
        AbstractAction action = null;
        switch (actionTypeEnum){
            case INSTALL_MASTER_NODE_1_28_2:
                action =
                        new ConfigHostNameAction(sshContext,
                        new ResetYumRepoAction(sshContext,
                        new ShutdownFirewallAction(sshContext,
                        new ShutdownSelinuxAction(sshContext,
                        new ShutdownSwapAction(sshContext,
                        new InstallContainerdAction(sshContext,
                        new SetKernelConfigAction(sshContext,
                        new InstallKube_1_28_2_Action(sshContext,
                        new ConfigMasterNode_1_28_2_Action(sshContext,
                        new CheckTaintAction(sshContext,
                        new InstallCalicoAction(sshContext,
                        new InstallMetricsAction(sshContext,
                        new InstallIngressAction(sshContext,
                        new InstallDashboardAction(sshContext,
                        new PrintMaterTokenAction(sshContext,
                        new InstallMetallbAction(sshContext,
                        new InstallHelmAction(sshContext,
                        new CompleteAction())))))))))))))))));
                break;
            case JOIN_IN_1_28_2:
                action =
                        new ConfigHostNameAction(sshContext,
                        new ResetYumRepoAction(sshContext,
                        new ShutdownFirewallAction(sshContext,
                        new ShutdownSelinuxAction(sshContext,
                        new ShutdownSwapAction(sshContext,
                        new InstallContainerdAction(sshContext,
                        new SetKernelConfigAction(sshContext,
                        new InstallKube_1_28_2_Action(sshContext,
                        new JoinAsNode_1_28_2_Action(sshContext,
                        new CompleteAction()).addCmd(cmds)))))))));
                break;
            default:
                action = new NullAction();
        }
        return action;
    }
}
