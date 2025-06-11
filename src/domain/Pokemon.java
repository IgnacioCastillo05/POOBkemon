package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

public class Pokemon implements Serializable {
    private static final long serialVersionUID = 936034061694635879L;
    
    private int ps;
    private int maxPs;
    private int attack;
    private int defense;
    private int speed;
    private int specialAttack;
    private int specialDefense;
    private String name;
    private Tipo tipo1;
    private Tipo tipo2;
    private List<Movimiento> movimientos;
    private String status;
    private static final int level = 100;
    private double multiplicadorPrecision = 1.0;
    private static final Map<String, EfectoEstadoStrategy> estrategiasEstado = new HashMap<>();
    private EfectoEstadoStrategy estrategiaEstadoActual;
    static {
            estrategiasEstado.put("quemado", new EfectoQuemadoStrategy());
            estrategiasEstado.put("envenenado", new EfectoEnvenenadoStrategy());
            estrategiasEstado.put("gravemente envenenado", new EfectoGraveEnvenenadoStrategy());
            estrategiasEstado.put("confundido", new EfectoConfundidoStrategy());
            estrategiasEstado.put("paralizado", new EfectoParalizadoStrategy());
            estrategiasEstado.put("dormido", new EfectoDormidoStrategy());
            estrategiasEstado.put("congelado", new EfectoCongeladoStrategy());
            estrategiasEstado.put(null, new EfectoNuloStrategy());
        }

    /**
     * Método constructor de la clase abstracta Pokemon. 
     * @param name Nombre del Pokémon.
     * @param ps Puntos de salud del Pokémon.
     * @param attack Ataque del Pokémon.
     * @param defense Defensa del Pokémon.
     * @param speed Velocidad del Pokémon.
     * @param specialAttack Ataque especial del Pokémon.
     * @param specialDefense Defensa especial del Pokémon.
     * @param tipo1 Tipo 1 del Pokémon.
     * @param tipo2 Tipo 2 del Pokémon.
     * Se toma en cuenta para el pokemon el tipo principal del pokemon. El tipo secundario puede ser nulo para los pokémon que no tienen un segundo tipo.
     * Para las primeras tres generaciones de pokemon (Kanto, Johto y Hoenn) se toma el tipo secundario como el tipo principal.
     */
    public Pokemon(int ps, int maxPs, int attack, int defense, int speed, int specialAttack, int specialDefense, String name, Tipo tipo1, Tipo tipo2) {
        this.ps = ps;
        this.maxPs = ps;    
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.name = name;
        this.tipo1 = tipo1;
        this.tipo2 = tipo2;
        this.status = null;
    }

    public Pokemon(int ps, int maxPs, int attack, int defense, int speed, int specialAttack, int specialDefense, String name, Tipo tipo1, Tipo tipo2, List<Movimiento> movimientos) {
        this(ps, maxPs, attack, defense, speed, specialAttack, specialDefense, name, tipo1, tipo2);
        this.movimientos = movimientos;
    }

    /**
     * Constructor de copia para crear un nuevo Pokémon con los mismos atributos
     * pero como una instancia independiente en memoria.
     * @param original El Pokémon original a copiar
     */
    public Pokemon(Pokemon original) {
        this.ps = original.ps;
        this.maxPs = original.maxPs;
        this.attack = original.attack;
        this.defense = original.defense;
        this.speed = original.speed;
        this.specialAttack = original.specialAttack;
        this.specialDefense = original.specialDefense;
        this.name = original.name;
        this.tipo1 = original.tipo1;
        this.tipo2 = original.tipo2;
        this.status = original.status;
        this.multiplicadorPrecision = original.multiplicadorPrecision;
        
        if (original.movimientos != null) {
            this.movimientos = new ArrayList<>();
            for (Movimiento movimientoOriginal : original.movimientos) {
                Movimiento copiaMov = null;
                
                if (movimientoOriginal instanceof MovimientoFisico) {
                    copiaMov = new MovimientoFisico((MovimientoFisico) movimientoOriginal);
                } else if (movimientoOriginal instanceof MovimientoEspecial) {
                    copiaMov = new MovimientoEspecial((MovimientoEspecial) movimientoOriginal);
                } else if (movimientoOriginal instanceof MovimientoEstado) {
                    copiaMov = new MovimientoEstado((MovimientoEstado) movimientoOriginal);
                }
                
                if (copiaMov != null) {
                    this.movimientos.add(copiaMov);
                }
            }
        }
    }

    public int getPs() {
        return ps;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public int getSpecialDefense() {
        return specialDefense;
    }

    public String getName() {
        return name;
    }

    public Tipo getTipo1() {
        return tipo1;
    }

    public Tipo getTipo2() {
        return tipo2;
    }

    public String getStatus() {
        return status;
    }
    
    public static int getLevel() {
        return level;
    }

    public double getMultiplicadorPrecision() {
        return multiplicadorPrecision;
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void addMovimiento(Movimiento movimiento) {
        if (movimientos.size() < 4) {
            movimientos.add(movimiento);
        } else {
            throw new IllegalStateException("Un Pokémon no puede tener más de 4 movimientos.");
        }
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setSpecialAttack(int specialAttack) {
        this.specialAttack = specialAttack;
    }

    public void setSpecialDefense(int specialDefense) {
        this.specialDefense = specialDefense;
    }

    public void setEstado(String estado) {
        this.status = estado;
        
        if (estado != null) {
            this.estrategiaEstadoActual = estrategiasEstado.getOrDefault(estado, new EfectoNuloStrategy());
        } else {
            this.estrategiaEstadoActual = estrategiasEstado.get(null);
        }
    }

    public boolean tieneEstado() {
        return status != null;
    }

    public void establecerEstado(String nuevoEstado) {
        if ("congelado".equals(nuevoEstado)) {
            boolean esDeHielo = false;
            
            if (tipo1 != null && tipo1.getNombre().equalsIgnoreCase("Hielo")) {
                esDeHielo = true;
            }
            
            if (tipo2 != null && tipo2.getNombre().equalsIgnoreCase("Hielo")) {
                esDeHielo = true;
            }
            
            if (esDeHielo) {
                return;
            }
        }
        
        if (status != null && estrategiaEstadoActual != null) {
            estrategiaEstadoActual.alCurar(this);
        }
        
        this.status = nuevoEstado;
        
        if (nuevoEstado != null) {
            this.estrategiaEstadoActual = estrategiasEstado.getOrDefault(nuevoEstado, new EfectoNuloStrategy());
        } else {
            this.estrategiaEstadoActual = estrategiasEstado.get(null);
        }
    }

    public boolean puedeAtacar() {
        if (status == null) return true;
        
        if (estrategiaEstadoActual == null) {
            estrategiaEstadoActual = estrategiasEstado.getOrDefault(status, new EfectoNuloStrategy());
        }
        
        return estrategiaEstadoActual.puedeAtacar(this);
    }

    public String aplicarEfectosEstado() {
        if (status == null) return "";
        
        if (estrategiaEstadoActual == null) {
            estrategiaEstadoActual = estrategiasEstado.getOrDefault(status, new EfectoNuloStrategy());
        }
        String resultado = estrategiaEstadoActual.aplicar(this);
        
        if (status == null && estrategiaEstadoActual != estrategiasEstado.get(null)) {
            estrategiaEstadoActual = estrategiasEstado.get(null);
        }
        if (ps <= 0) {
            status = "debilitado";
            estrategiaEstadoActual = estrategiasEstado.get(null);
        }
        return resultado;
    }

    public String procesarIntentarAtacar() {
        if (status == null) return null;
        
        if (estrategiaEstadoActual == null) {
            estrategiaEstadoActual = estrategiasEstado.getOrDefault(status, new EfectoNuloStrategy());
        }
        
        return estrategiaEstadoActual.procesarIntentarAtacar(this);
    }

    public void curarEstados() {
        if (status != null && estrategiaEstadoActual != null) {
            estrategiaEstadoActual.alCurar(this);
        }
        status = null;
        estrategiaEstadoActual = estrategiasEstado.get(null);
    }

    public void recibirDaño(int daño) {
        this.ps = Math.max(0, this.ps - daño);
    }
    
    public void curar(int cantidad) {
        this.ps = Math.min(this.ps + cantidad, this.maxPs);
    }
    
    public void revivir(int cantidad) {
        if (this.ps <= 0) {
            this.ps = cantidad;
        }
    }
    
    public boolean estaDebilitado() {
        return this.ps <= 0;
    }
    
    public int getMaxPs() {
        return this.maxPs;
    }
    public void reducirPrecision(double factor) {
        this.multiplicadorPrecision *= factor;
    }
    public void aumentarPrecision(double factor) {
        this.multiplicadorPrecision *= factor;
    }
}