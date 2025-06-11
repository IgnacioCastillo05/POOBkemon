package domain;

import java.util.*;

/**
 * Entrenador máquina con estrategia defensiva.
 * Prioriza movimientos de estado, mejoras defensivas y resistencia.
 */
public class DefensiveTrainer extends EntrenadorMaquina {
    
    public DefensiveTrainer(String nombre) {
        super(nombre, "defensive");
    }
    
    @Override
    protected Pokemon elegirMejorPokemon() {
        if (!tieneMasDeUnPokemonDisponible()) {
            return getPrimerPokemonDisponible();
        }

        Pokemon mejorPokemon = null;
        int mayorEstadisticaDefensa = 0;
        
        for (Pokemon pokemon : getPokemones()) {
            if (!pokemon.estaDebilitado()) {
                int estadisticaDefensa = Math.max(pokemon.getDefense(), pokemon.getSpecialDefense());
                int factorDefensivo = estadisticaDefensa + (pokemon.getPs() / 2);
                
                if (mejorPokemon == null || factorDefensivo > mayorEstadisticaDefensa) {
                    mejorPokemon = pokemon;
                    mayorEstadisticaDefensa = factorDefensivo;
                }
            }
        }
        
        return mejorPokemon;
    }
    
    @Override
    protected Movimiento elegirMejorMovimiento(Pokemon pokemon) {
        List<Movimiento> movimientos = pokemon.getMovimientos();
        
        if (movimientos == null || movimientos.isEmpty()) {
            return null;
        }

        List<Movimiento> movimientosDisponibles = filtrarMovimientosConPP(movimientos);
        if (movimientosDisponibles.isEmpty()) {
            return null;
        }
        
        return elegirMejorMovimiento(pokemon, movimientosDisponibles);
    }
    
    /**
     * Actualizar elegirMejorMovimiento en DefensiveTrainer para usar la nueva firma
     */
    @Override
    protected Movimiento elegirMejorMovimiento(Pokemon pokemon, List<Movimiento> movimientosDisponibles) {
        if (pokemon.getPs() < pokemon.getMaxPs() * 0.6) {
            for (Movimiento movimiento : movimientosDisponibles) {
                String nombreMovimiento = movimiento.getNombre();
                if (movimiento instanceof MovimientoEstado && 
                    nombreMovimiento.equals("Respiro")) {
                    return movimiento;
                }
            }
        }
        for (Movimiento movimiento : movimientosDisponibles) {
            String nombreMovimiento = movimiento.getNombre();
            if (movimiento instanceof MovimientoEstado && 
                (nombreMovimiento.equals("Refugio") || 
                 nombreMovimiento.equals("Velo Aurora") ||
                 nombreMovimiento.equals("Corpulencia") ||
                 nombreMovimiento.equals("Encanto") ||
                 nombreMovimiento.equals("Eco Metalico") ||
                 nombreMovimiento.equals("Ataque Arena"))) {
                
                return movimiento;
            }
        }
        for (Movimiento movimiento : movimientosDisponibles) {
            String nombreMovimiento = movimiento.getNombre();
            if (movimiento instanceof MovimientoEstado && 
                (nombreMovimiento.equals("Paralizador") || 
                 nombreMovimiento.equals("Tóxico") ||
                 nombreMovimiento.equals("Fuego Fatuo") ||
                 nombreMovimiento.equals("Hipnosis") ||
                 nombreMovimiento.equals("Onda Trueno") ||
                 nombreMovimiento.equals("Rayo Confuso"))) {
                
                return movimiento;
            }
        }
        Movimiento mejorMovimientoOfensivo = null;
        double mejorPuntaje = 0;
        
        for (Movimiento movimiento : movimientosDisponibles) {
            if (movimiento instanceof MovimientoFisico || movimiento instanceof MovimientoEspecial) {
                double puntaje = calcularPuntajeDefensivo(movimiento, pokemon);
                
                if (mejorMovimientoOfensivo == null || puntaje > mejorPuntaje) {
                    mejorMovimientoOfensivo = movimiento;
                    mejorPuntaje = puntaje;
                }
            }
        }
        
        if (mejorMovimientoOfensivo != null) {
            return mejorMovimientoOfensivo;
        }
        return movimientosDisponibles.get(random.nextInt(movimientosDisponibles.size()));
    }
    
    /**
     * Calcula un puntaje para un movimiento basado en criterios defensivos
     */
    private double calcularPuntajeDefensivo(Movimiento movimiento, Pokemon pokemon) {
        int poder = movimiento.getPoder();
        double puntaje = poder * 0.7;

        String nombreMovimiento = movimiento.getNombre();
        if (nombreMovimiento.equals("Triturar") ||
            nombreMovimiento.equals("Fuerza Lunar") ||
            nombreMovimiento.equals("Disparo Lodo") ||
            nombreMovimiento.equals("Bola Sombra") ||
            nombreMovimiento.equals("Psíquico") ||
            nombreMovimiento.equals("Foco Resplandor") ||
            nombreMovimiento.equals("Carantoña")) {
            
            puntaje *= 1.3;
        }

        if (nombreMovimiento.equals("Lanzallamas") ||
            nombreMovimiento.equals("Rayo") ||
            nombreMovimiento.equals("Huracán") ||
            nombreMovimiento.equals("Rayo Hielo") ||
            nombreMovimiento.equals("Bomba Lodo") ||
            nombreMovimiento.equals("Golpe Cuerpo") ||
            nombreMovimiento.equals("Puño Hielo") ||
            nombreMovimiento.equals("Puya Nociva")) {
            
            puntaje *= 1.4;
        }

        if (movimiento.getTipo().equals(pokemon.getTipo1()) || 
            (pokemon.getTipo2() != null && movimiento.getTipo().equals(pokemon.getTipo2()))) {
            puntaje *= 1.2;
        }

        int precision = movimiento.getPrecision();
        if (precision < 100 && precision > 0) {
            puntaje *= (precision / 100.0);
        }
        
        return puntaje;
    }
    
    @Override
    public int elegirAccion(Entrenador oponente, Scanner scanner) throws POOBkemonException {
        Pokemon miPokemon = getPrimerPokemonDisponible();
        if (miPokemon.getPs() < miPokemon.getMaxPs() * 0.5 && !getItems().isEmpty()) {
            for (Item item : getItems()) {
                if (item instanceof Potion) {
                    return 3;
                }
            }
        }

        if (miPokemon.getPs() < miPokemon.getMaxPs() * 0.3 && tieneMasDeUnPokemonDisponible()) {
            for (Pokemon pokemon : getPokemones()) {
                if (!pokemon.equals(miPokemon) && !pokemon.estaDebilitado() && 
                    pokemon.getPs() > pokemon.getMaxPs() * 0.5) {
                    return 2;
                }
            }
        }
        if (miPokemon.tieneEstado() && tieneOtroPokemonSinEstado(miPokemon)) {
            String estado = miPokemon.getStatus();
            if (estado.equals("quemado") || estado.equals("paralizado") || 
                estado.equals("envenenado") || estado.equals("dormido") || 
                estado.equals("congelado")) {
                return 2;
            }
        }

        return 1;
    }
    
    /**
     * Verifica si tenemos otro Pokémon disponible sin estados alterados
     */
    private boolean tieneOtroPokemonSinEstado(Pokemon pokemonActual) {
        for (Pokemon pokemon : getPokemones()) {
            if (!pokemon.equals(pokemonActual) && !pokemon.estaDebilitado() && !pokemon.tieneEstado()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected boolean deberiaUsarItem(Pokemon pokemon) {
        if (pokemon.getPs() < pokemon.getMaxPs() * 0.3) {
            return true;
        }
        if (pokemon.tieneEstado() && pokemon.getPs() < pokemon.getMaxPs() * 0.4) {
            String estado = pokemon.getStatus();
            if (estado.equals("quemado") || estado.equals("envenenado") || 
                estado.equals("gravemente envenenado")) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Item elegirItem() throws POOBkemonException {
        List<Item> items = getItems();
        
        if (items.isEmpty()) {
            throw new POOBkemonException("No hay items disponibles.");
        }
        Pokemon pokemon = getPrimerPokemonDisponible();
        if (pokemon.estaDebilitado()) {
            int pokemonDisponibles = 0;
            for (Pokemon p : getPokemones()) {
                if (!p.estaDebilitado()) {
                    pokemonDisponibles++;
                }
            }
            if (pokemonDisponibles <= 2) {
                for (Item item : items) {
                    if (item instanceof Revive) {
                        return item;
                    }
                }
            }
        }
        if (pokemon != null && !pokemon.estaDebilitado()) {
            int psActual = pokemon.getPs();
            int psMaximos = pokemon.getMaxPs();
            double porcentajeSalud = (double) psActual / psMaximos;

            if (porcentajeSalud < 0.3) {
                int psRestantes = psMaximos - psActual;

                if (psRestantes <= 25) {
                    for (Item item : items) {
                        if (item instanceof NormalPotion) {
                            return item;
                        }
                    }
                }
                if (psRestantes <= 55) {
                    for (Item item : items) {
                        if (item instanceof SuperPotion) {
                            return item;
                        }
                    }
                }
                if (psRestantes > 55) {
                    for (Item item : items) {
                        if (item instanceof HyperPotion) {
                            return item;
                        }
                    }
                }
            }
        }
        return items.get(0);
    }
}