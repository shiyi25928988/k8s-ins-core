package yi.shi.ssh.actions;


import yi.shi.ssh.shell.SshContext;

import java.util.Objects;

public abstract class AbstractAction implements Runnable {

    private SshContext sshContext;

    private AbstractAction nextAction = null;

    public AbstractAction(){}

    public AbstractAction(SshContext sshContext){
        this.sshContext = sshContext;
    }

    public AbstractAction(SshContext sshContext, AbstractAction action){
        this.sshContext = sshContext;
        this.nextAction = action;
    }

    public boolean hasNextAction(){
        return Objects.nonNull(this.nextAction);
    }

    public AbstractAction getNextAction(){
        return this.nextAction;
    }

    public void setNextAction(AbstractAction action){
        this.nextAction = action;
    }

    public abstract void execute();

    public SshContext getSshContext(){
        return sshContext;
    }

    public void setSshContext(SshContext sshContext){
        this.sshContext = sshContext;
    }

    public void close(){
        if(this.getSshContext().getSession().isConnected()){
            this.getSshContext().getSession().disconnect();
        }
    }

    @Override
    public void run() {
        execute();
        if(this.hasNextAction()){
            getNextAction().run();
        }
    }
}
