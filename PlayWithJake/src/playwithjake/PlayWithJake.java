/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playwithjake;

import DBA.SuperAgent;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nazaret
 */
public class PlayWithJake extends SuperAgent {

    ACLMessage outbox = null;
    ACLMessage inbox = null;
    String reply_with, conversation_id;
    int num;
    boolean canceled = false;

    public PlayWithJake(AgentID aid) throws Exception {
        super(aid);
        num = 50;
        outbox = new ACLMessage();
    }

    /**
     * @param args the command line arguments
     */
    
    public void execute() {

        System.out.print("execute");
        try {
            outbox = new ACLMessage();
            //Receiver
            outbox.addReceiver(new AgentID("Jake"));
            outbox.setSender(this.getAid());

            //outbox.setPerformative(ACLMessage.REQUEST); // int
            do {
                outbox.setPerformative(ACLMessage.REQUEST);
                if (canceled) {
                    outbox.setInReplyTo(reply_with);
                    outbox.setConversationId(conversation_id);
                }
                this.send(outbox);
                inbox = receiveACLMessage();

            } while (inbox.getPerformativeInt() == ACLMessage.REFUSE || inbox.getPerformativeInt() == ACLMessage.NOT_UNDERSTOOD);

        } catch (InterruptedException ex) {
            Logger.getLogger(PlayWithJake.class.getName()).log(Level.SEVERE, null, ex);
        }

        auxiliar();
    }

    public void auxiliar() {
        System.out.print("auxiliar");
        
        try {
            if (inbox.getPerformativeInt() == ACLMessage.AGREE) {
                //getreplywith -> set y reply to
                reply_with = inbox.getReplyWith();
                conversation_id = inbox.getConversationId();
                juego();
            } else if (inbox.getPerformativeInt() == ACLMessage.INFORM) {
                juego();
            } else if (inbox.getPerformativeInt() == ACLMessage.NOT_UNDERSTOOD) {
                outbox = new ACLMessage();
                outbox.setPerformative(ACLMessage.CANCEL);
                this.send(outbox);
                inbox = receiveACLMessage(); // espera
                // Tenemos que hacer el request con los datos que nos envía
                canceled = true;
                execute();
            } else {
                //Es el confirm
                System.out.println("confirm");
                if(canceled){
                    reply_with = inbox.getReplyWith();
                    conversation_id = inbox.getConversationId();
                    execute();
                    canceled = false;
                }
                System.out.println("Se ha ido");
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(PlayWithJake.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    public void juego() {
        System.out.print("juego");
        try {
            outbox = new ACLMessage(); // para que no haya errores con mensajes anteriores
            outbox.setPerformative(ACLMessage.QUERY_IF);
            outbox.setContent(String.valueOf(num)); // mandamos el número
            outbox.setInReplyTo(reply_with); // para decirle que le respondemos con ese tag
            outbox.setConversationId(conversation_id); //le damos el id

            this.send(outbox);

            inbox = receiveACLMessage(); // espera
            
            System.out.println(inbox.getPerformative());
            if(inbox.getPerformativeInt() == ACLMessage.INFORM){
                reply_with = inbox.getReplyWith();
                conversation_id = inbox.getConversationId();
                System.out.print("inform");
                if (inbox.getContent().equals("LOWER")) {
                    num = num / 2;
                } else {
                    num += num / 2;
                }
                System.out.println(String.valueOf(num));

                juego();
            }
            else{
                System.out.println("no es inform");
                auxiliar();
            }
            
        } catch (InterruptedException ex) {
            Logger.getLogger(PlayWithJake.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

    }

}
