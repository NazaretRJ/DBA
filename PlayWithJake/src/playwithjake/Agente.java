/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playwithjake;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;

/**
 *
 * @author Nazaret
 */
public class Agente {
    PlayWithJake mi_agente;
    public static void main(String[] args) {
        // TODO code application logic here
        AgentsConnection.connect("isg2.ugr.es",6000, "Practica2", "guest", "guest", false);
        PlayWithJake mi_agente;
        
        
        try {
              // Crear agentes y arrancarlos
              mi_agente = new PlayWithJake(new AgentID("RUIZ JALDO, MARIA NAZARET"));
              mi_agente.start();
              
          } catch (Exception ex) {
              ex.printStackTrace();
          }

    }
}
