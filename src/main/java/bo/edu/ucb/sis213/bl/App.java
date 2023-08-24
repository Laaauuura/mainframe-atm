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

    public boolean validarCredenciales(String username, int password) {
            final int a = us.validarCredenciales(username, password);
            boolean flag = false;
            if(a>-1){
               JOptionPane.showMessageDialog(null,  "¡Bienvenido, " + username + "!\nHas iniciado sesión correctamente.\nInicio de Sesión Exitoso");
                flag=true;
            }else{
                intentosAcceso--;
                JOptionPane.showMessageDialog(null, "PIN incorrecto. Tiene: "+intentosAcceso+" intentos");

                if(intentosAcceso==0){
                    JOptionPane.showMessageDialog(null, "PIN incorrecto, ya no puede ingresar");
                    flag = false;

                }
            }
            return flag;
        }
    
    public void setPinAc(String username, int password){
            pinActual= us.pinAct(username);
        }

    public void setId(String username, int password){
            usuarioId = us.validarCredenciales(username, password);
    }

    public void setSaldo(String username, int password){
        saldo = us.consultarSaldo(usuarioId);
    }

    public double saldo(){
        return saldo;
    }
    

    public  void realizarDeposito(double cantidad) {
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(null, "Cantidad no válida");
        } else {
            saldo=saldo+cantidad;
            his.registrarOperacion(usuarioId, "Depósito", cantidad);
            us.realizarOp(usuarioId, saldo);

            JOptionPane.showMessageDialog(null, "Realizado\nEl nuevo saldo es: "+saldo);
                } 
    }

    public  void realizarRetiro(double cantidad) {
       if (cantidad <= 0) {
            JOptionPane.showMessageDialog(null, "Cantidad no válida");
        } else if(saldo<cantidad){
            JOptionPane.showMessageDialog(null, "Saldo insuficiente");
                } else{
                    saldo=saldo-cantidad;
            his.registrarOperacion(usuarioId, "Retiro", cantidad);
            us.realizarOp(usuarioId, cantidad);

            JOptionPane.showMessageDialog(null, "Realizado\nEl nuevo saldo es: "+saldo);
                }
    }

    public  void cambiarPIN(int nuevoPin, int confirmacionPin) {
                if (nuevoPin == confirmacionPin) {
                            pinActual = nuevoPin;
                            us.cambiarPIN(usuarioId, nuevoPin);
                            JOptionPane.showMessageDialog(null, "PIN actualizado con éxito.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Los PINs no coinciden.");
                        }
                
    }
    public boolean validarPin(int pinAct, int pin){
        if(pinAct==pin){
            
            return true;
        }
        else{
        
            return false;
        }
    }

    public  void registrarOperacion(String tipoOperacion, double cantidad) {
        his.registrarOperacion(usuarioId, tipoOperacion, cantidad);

    }
    public  void mostrarHistorial(int usuarioId) {
        his.obtenerHistorial(usuarioId);

    }

    public  void consultaSaldo(int usuarioId) {
        us.consultarSaldo(usuarioId);

    }

}
