package domain;

public class MovimientoEstado extends Movimiento {
    private static final long serialVersionUID = 7775130181025684876L;
    public MovimientoEstado(String nombre, Tipo tipo, int poder, int precision, int ppActual, int ppMaximo, boolean prioridad, String claseMovimiento, String descripcion) {
        super(nombre, tipo, poder, precision, ppActual, ppMaximo, prioridad, claseMovimiento, descripcion);
    }

    public MovimientoEstado(MovimientoEstado original) {
        super(original);
    }

    @Override
    public String toString() {
        return "Movimiento Estado: " + super.toString();
    }

    @Override
    public String aplicarEfecto(Pokemon usuario, Pokemon objetivo) {
        String nombreMovimiento = this.getNombre();
        String resultado = "";
        
        switch (nombreMovimiento) {
            case "Tóxico":
                if (!objetivo.tieneEstado()) {
                    objetivo.setEstado("gravemente envenenado");
                    resultado = "¡" + objetivo.getName() + " ha sido gravemente envenenado!";
                } else {
                    resultado = "No tuvo efecto en " + objetivo.getName();
                }
                break;
                
            case "Hipnosis":
                if (!objetivo.tieneEstado()) {
                    objetivo.setEstado("dormido");
                    resultado = "¡" + objetivo.getName() + " se ha dormido!";
                } else {
                    resultado = "No tuvo efecto en " + objetivo.getName();
                }
                break;
                
            case "Danza Dragon":
                usuario.setAttack((int)(usuario.getAttack() * 1.2));
                usuario.setSpeed((int)(usuario.getSpeed() * 1.2));
                resultado = "¡" + usuario.getName() + " aumentó su ataque y velocidad!";
                break;

            case "Refugio":
                usuario.setDefense((int)(usuario.getDefense() * 1.2));
                resultado = "¡" + usuario.getName() + " aumentó su defensa!";
                break;

            case "Eco Metalico":
                objetivo.setSpecialDefense((int)(objetivo.getSpecialDefense() * 0.8));
                resultado = "¡" + objetivo.getName() + " disminuyó su defensa especial!";
                break;
                
            case "Pulimento":
                usuario.setSpeed((int)(usuario.getSpeed() * 1.2));
                resultado = "¡" + usuario.getName() + " aumentó su velocidad!";
                break;

            case "Tambor":
                usuario.setPs(usuario.getPs() / 2);
                usuario.setAttack(usuario.getAttack() * 2);
                resultado = "¡" + usuario.getName() + " aumentó su ataque al máximo y redujo sus PS a la mitad!";
                break;

            case "Velo Aurora":
                usuario.setSpecialDefense((int)(usuario.getSpecialDefense() * 1.2));
                usuario.setDefense((int)(usuario.getDefense() * 1.2));
                resultado = "¡" + usuario.getName() + " aumentó su defensa especial y defensa!";
                break;

            case "Ataque Arena":
                objetivo.reducirPrecision(0.9);
                resultado = "¡La precisión de " + objetivo.getName() + " ha disminuido!";
                break;
            
            case "Paralizador":
                if (!objetivo.tieneEstado()) {
                    objetivo.setEstado("paralizado");
                    resultado = "¡" + objetivo.getName() + " ha sido paralizado!";
                } else {
                    resultado = "No tuvo efecto en " + objetivo.getName();
                }
                break;

            case "Rayo Confuso":
                if (!objetivo.tieneEstado()) {
                    objetivo.setEstado("confundido");
                    resultado = "¡" + objetivo.getName() + " ha sido confundido!";
                } else {
                    resultado = "No tuvo efecto en " + objetivo.getName();
                }
                break;

            case "Respiro":
                int restaurarPs = Math.max(1, usuario.getMaxPs() / 2);
                usuario.setPs(Math.min(usuario.getPs() + restaurarPs, usuario.getMaxPs()));
                resultado = "¡" + usuario.getName() + " ha restaurado " + restaurarPs + " PS!";
                break;

            case "Fuegofatuo":
                if (!objetivo.tieneEstado()) {
                    objetivo.setEstado("quemado");
                    resultado = "¡" + objetivo.getName() + " ha sido quemado!";
                }
                break;

            case "Corpulencia":
                usuario.setDefense((int)(usuario.getDefense() * 1.2));
                usuario.setAttack((int)(usuario.getAttack() * 1.2));
                resultado = "¡" + usuario.getName() + " aumentó su defensa y ataque!";
                break;

            case "Encanto":
                objetivo.setAttack((int)(objetivo.getAttack() * 0.8));
                resultado = "¡El ataque de " + objetivo.getName() + " ha disminuido!";
                break;
        
            case "Onda Trueno":
                if (!objetivo.tieneEstado()) {
                    objetivo.setEstado("paralizado");
                    resultado = "¡" + objetivo.getName() + " ha sido paralizado!";
                } else {
                    resultado = "No tuvo efecto en " + objetivo.getName();
                }
                break;

            case "Afilagarras":
                usuario.setAttack((int)(usuario.getAttack() * 1.2));
                resultado = "¡" + usuario.getName() + " aumentó su ataque!";
                break;

            case "Disparo Demora":
                objetivo.setSpeed((int)(objetivo.getSpeed() * 0.8));
                resultado = "¡La velocidad de " + objetivo.getName() + " ha disminuido!";
                break;

            default:
                resultado = "No se aplicó ningún efecto especial.";
                break;
        }
        
        return resultado;
    }
}