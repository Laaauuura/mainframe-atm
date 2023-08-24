package bo.edu.ucb.sis213.bl;

import javax.swing.JOptionPane;

import bo.edu.ucb.sis213.dao.functionsHisAtm;
import bo.edu.ucb.sis213.dao.functionsUsAtm;


public class App {

    functionsHisAtm his = new functionsHisAtm();
    functionsUsAtm us = new functionsUsAtm();
    
    private  int  pinActual;
    private  int  usuarioId;  
    private  double saldo; 
    private  int intentosAcceso = 3;

    //al mismo tiempo saca el id del usuario
    public void validarCredenciales(String username, int password) {
            final int a = us.validarCredenciales(username, password);
            if(a>-1){
               JOptionPane.showMessageDialog(null, "PIN correcto, bienvenido");
            }else{
                intentosAcceso--;
                JOptionPane.showMessageDialog(null, "PIN incorrecto. Tiene: "+intentosAcceso+" intentos");

                if(intentosAcceso==0){
                    JOptionPane.showMessageDialog(null, "PIN incorrecto, ya no puede ingresar");

                }
            }
        }
    
    public void datos(String username, int password){
            pinActual= us.pinAct(username);
            usuarioId = us.validarCredenciales(username, password);
            saldo = us.consultarSaldo(usuarioId);

        }
    

    public  void realizarDeposito(double cantidad) {
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(null, "Cantidad no válida");
        } else {
            JOptionPane.showMessageDialog(null, "Deposito");
            us.realizarDeposito(usuarioId, cantidad);
            registrarOperacion("Depósito", cantidad);
                } 
    }

    public  void realizarRetiro(double cantidad) {
       if (cantidad <= 0) {
            JOptionPane.showMessageDialog(null, "Cantidad no válida");
        } else if(saldo<cantidad){
            JOptionPane.showMessageDialog(null, "Saldo insuficiente");
                } else{
                    us.realizarRetiro(usuarioId, cantidad);
                    registrarOperacion("Retiro", cantidad);
                }
    }

    public  void cambiarPIN(int nuevoPin, int confirmacionPin) {
        if (nuevoPin == pinActual) {
                if (nuevoPin == confirmacionPin) {
                            pinActual = nuevoPin;
                            us.cambiarPIN(usuarioId, nuevoPin);
                            JOptionPane.showMessageDialog(null, "PIN actualizado con éxito.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Los PINs no coinciden.");
                        }

                } else {
                    JOptionPane.showMessageDialog(null, "PIN incorrecto.");
                }
    }

    private  void registrarOperacion(String tipoOperacion, double cantidad) {
        his.registrarOperacion(usuarioId, tipoOperacion, cantidad);

    }
    private  void mostrarHistorial(int usuarioId) {
        his.obtenerHistorial(usuarioId);

    }

    private  void consultaSaldo(int usuarioId) {
        us.consultarSaldo(usuarioId);

    }

}
