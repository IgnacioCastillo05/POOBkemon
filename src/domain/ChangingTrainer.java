package domain;

import java.util.*;

/**
 * Entrenador máquina que cambia constantemente de Pokémon.
 * Prioriza tener ventaja de tipo contra el oponente.
 */
public class ChangingTrainer extends EntrenadorMaquina {
    
    private Pokemon lastPokemonOponente;
    
    public ChangingTrainer(String nombre) {
        super(nombre, "changing");
        this.lastPokemonOponente = null;
    }
    
    @Override
    protected Pokemon elegirMejorPokemon() {
        if (!tieneMasDeUnPokemonDisponible()) {
            return getPrimerPokemonDisponible();
        }

        if (lastPokemonOponente == null) {
            List<Pokemon> disponibles = new ArrayList<>();
            for (Pokemon p : getPokemones()) {
                if (!p.estaDebilitado()) {
                    disponibles.add(p);
                }
            }
            return disponibles.get(random.nextInt(disponibles.size()));
        }

        Pokemon mejorPokemon = null;
        double mejorEfectividad = 0;
        
        for (Pokemon pokemon : getPokemones()) {
            if (!pokemon.estaDebilitado()) {
                double efectividad = calcularEfectividadTotal(pokemon, lastPokemonOponente);
                
                if (mejorPokemon == null || efectividad > mejorEfectividad) {
                    mejorPokemon = pokemon;
                    mejorEfectividad = efectividad;
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
    
    @Override
    protected Movimiento elegirMejorMovimiento(Pokemon pokemon, List<Movimiento> movimientosDisponibles) {
        if (lastPokemonOponente == null) {
            return movimientosDisponibles.get(random.nextInt(movimientosDisponibles.size()));
        }
        Movimiento mejorMovimiento = null;
        double mejorEfectividad = 0;
        
        for (Movimiento movimiento : movimientosDisponibles) {
            if (movimiento instanceof MovimientoFisico || movimiento instanceof MovimientoEspecial) {
                double efectividad = calcularEfectividadMovimiento(movimiento, lastPokemonOponente);
                
                if (mejorMovimiento == null || efectividad > mejorEfectividad) {
                    mejorMovimiento = movimiento;
                    mejorEfectividad = efectividad;
                }
            }
        }

        if (mejorMovimiento == null) {
            for (Movimiento movimiento : movimientosDisponibles) {
                if (movimiento instanceof MovimientoEstado) {
                    String nombreMovimiento = movimiento.getNombre();
                    if (nombreMovimiento.equals("Danza Dragon") || 
                        nombreMovimiento.equals("Pulimento") || 
                        nombreMovimiento.equals("Afilagarras")) {
                        return movimiento;
                    }
                }
            }

            return movimientosDisponibles.get(random.nextInt(movimientosDisponibles.size()));
        }
        
        return mejorMovimiento;
    }
    
    @Override
    public int elegirAccion(Entrenador oponente, Scanner scanner) throws POOBkemonException {
        Pokemon pokemonOponente = oponente.getPrimerPokemonDisponible();
        Pokemon miPokemon = getPrimerPokemonDisponible();

        lastPokemonOponente = pokemonOponente;

        if (miPokemon.getPs() < miPokemon.getMaxPs() * 0.2 && !getItems().isEmpty()) {
            for (Item item : getItems()) {
                if (item instanceof Potion) {
                    return 3;
                }
            }
        }

        if (tieneMasDeUnPokemonDisponible()) {
            double efectividadOfensiva = calcularEfectividadOfensiva(miPokemon, pokemonOponente);
            double efectividadDefensiva = calcularEfectividadDefensiva(miPokemon, pokemonOponente);

            boolean tieneDesventajaTipo = efectividadDefensiva > 1.0;
            boolean noTieneVentajaTipo = efectividadOfensiva <= 1.0;
            boolean cambioAleatorio = random.nextDouble() < 0.4; 

            if (tieneDesventajaTipo || (noTieneVentajaTipo && cambioAleatorio)) {
                Pokemon mejorAlternativa = encontrarMejorAlternativa(pokemonOponente, miPokemon);
                
                if (mejorAlternativa != null) {
                    return 2;
                }
            }
        }
        return 1;
    }
    
    /**
     * Encuentra la mejor alternativa de Pokémon para enfrentar al oponente
     */
    private Pokemon encontrarMejorAlternativa(Pokemon oponente, Pokemon actual) {
        Pokemon mejorAlternativa = null;
        double mejorPuntaje = calcularPuntajeGeneral(actual, oponente);
        
        for (Pokemon pokemon : getPokemones()) {
            if (!pokemon.equals(actual) && !pokemon.estaDebilitado()) {
                double puntaje = calcularPuntajeGeneral(pokemon, oponente);

                if (puntaje > mejorPuntaje * 1.3) {
                    mejorAlternativa = pokemon;
                    mejorPuntaje = puntaje;
                }
            }
        }
        
        return mejorAlternativa;
    }
    
    /**
     * Calcula efectividad ofensiva simplificada
     */
    private double calcularEfectividadOfensiva(Pokemon atacante, Pokemon defensor) {
        double efectividad = 1.0;
        
        if (atacante.getTipo1() != null) {
            efectividad = Math.max(efectividad, 
                atacante.getTipo1().calcularEfectividadContraPokemon(defensor));
        }
        
        if (atacante.getTipo2() != null) {
            efectividad = Math.max(efectividad, 
                atacante.getTipo2().calcularEfectividadContraPokemon(defensor));
        }
        
        return efectividad;
    }

    /**
     * Calcula efectividad defensiva simplificada
     */
    private double calcularEfectividadDefensiva(Pokemon miPokemon, Pokemon oponente) {
        double efectividad = 1.0;
        
        if (oponente.getTipo1() != null) {
            efectividad = Math.max(efectividad, 
                oponente.getTipo1().calcularEfectividadContraPokemon(miPokemon));
        }
        
        if (oponente.getTipo2() != null) {
            efectividad = Math.max(efectividad, 
                oponente.getTipo2().calcularEfectividadContraPokemon(miPokemon));
        }
        
        return efectividad;
    }

    /**
     * Calcula un puntaje general más simple para evaluar matchups
     */
    private double calcularPuntajeGeneral(Pokemon miPokemon, Pokemon oponente) {
        double puntaje = 0;

        double ventajaOfensiva = calcularEfectividadOfensiva(miPokemon, oponente);
        puntaje += ventajaOfensiva * 40;

        double desventajaDefensiva = calcularEfectividadDefensiva(miPokemon, oponente);
        puntaje += (2.0 - desventajaDefensiva) * 30;

        if (miPokemon.getSpeed() > oponente.getSpeed()) {
            puntaje += 20;
        }
        
        double porcentajeSalud = (double) miPokemon.getPs() / miPokemon.getMaxPs();
        puntaje += porcentajeSalud * 10;
        
        return puntaje;
    }
    
    /**
     * Calcula la efectividad total de un Pokémon contra otro basado en sus tipos
     */
    private double calcularEfectividadTotal(Pokemon atacante, Pokemon defensor) {
        if (atacante == null || defensor == null) {
            return 1.0;
        }
        double efectividadTotal = 1.0;
        if (atacante.getTipo1() != null) {
            efectividadTotal *= atacante.getTipo1().calcularEfectividadContraPokemon(defensor);
        }
        if (atacante.getTipo2() != null) {
            efectividadTotal *= atacante.getTipo2().calcularEfectividadContraPokemon(defensor);
        }
        if (defensor.getTipo1() != null) {
            double defensaT1 = defensor.getTipo1().calcularEfectividadContraPokemon(atacante);
            efectividadTotal /= (defensaT1 > 0 ? defensaT1 : 1.0);
        }
        if (defensor.getTipo2() != null) {
            double defensaT2 = defensor.getTipo2().calcularEfectividadContraPokemon(atacante);
            efectividadTotal /= (defensaT2 > 0 ? defensaT2 : 1.0);
        }
        return efectividadTotal;
    }
    
    /**
     * Calcula la efectividad de un movimiento contra un Pokémon
     */
    private double calcularEfectividadMovimiento(Movimiento movimiento, Pokemon defensor) {
        if (movimiento == null || defensor == null) {
            return 1.0;
        }
        double efectividad = movimiento.getTipo().calcularEfectividadContraPokemon(defensor);
        efectividad *= movimiento.getPoder();
        int precision = movimiento.getPrecision();
        if (precision < 100 && precision > 0) {
            efectividad *= (precision / 100.0);
        }
        return efectividad;
    }
    
    @Override
    protected boolean deberiaUsarItem(Pokemon pokemon) {
        return pokemon.getPs() < pokemon.getMaxPs() * 0.25;
    }
    
    @Override
    public Item elegirItem() throws POOBkemonException {
        List<Item> items = getItems();
        
        if (items.isEmpty()) {
            throw new POOBkemonException("No hay items disponibles.");
        }
        
        Pokemon pokemon = getPrimerPokemonDisponible();

        if (pokemon.estaDebilitado()) {
            for (Item item : items) {
                if (item instanceof Revive) {
                    return item;
                }
            }
        }

        for (Item item : items) {
            if (item instanceof HyperPotion) {
                return item;
            }
        }
        
        for (Item item : items) {
            if (item instanceof SuperPotion) {
                return item;
            }
        }
        
        for (Item item : items) {
            if (item instanceof NormalPotion) {
                return item;
            }
        }

        return items.get(0);
    }
}