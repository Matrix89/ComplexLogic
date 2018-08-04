package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class DivisionLogic extends BundledGateLogic{
        @Override
        public Connection getType(EnumFacing dir) {
            switch (dir) {
                case NORTH:
                    return Connection.OUTPUT_BUNDLED;
                case EAST:
                case WEST:
                    return Connection.INPUT_BUNDLED;
                case SOUTH:
                    return Connection.OUTPUT;
                default:
                    return Connection.NONE;
            }
        }

        @Override
        public boolean tick(PartGate parent) {
            byte[] inA = parent.getBundledInput(EnumFacing.WEST);
            byte[] inB = parent.getBundledInput(EnumFacing.EAST);
            int a = bundledRsToDigi(inA);
            int b = bundledRsToDigi(inB);
            int v = 0;
            try {
                v = a / b;
            }catch (ArithmeticException e){
                outputValues[2] = 15;
            }
            setBundledValue(EnumFacing.NORTH, bundledDigiToRs(v));
            return super.tick(parent);
        }

        @Override
        public State getTorchState(int i) {
            return State.ON;
        }

        @Override
        public State getLayerState(int i) {
            return getOutputValueInside(EnumFacing.SOUTH) != 0 ? State.ON : State.OFF;
        }

}
