private static class combineExample extends CombineFn<inputType, AccumType, outputType> {

    public AccumType createAccumulator() {
        return null;
    }

    public AccumType addInput(AccumType accum, inputType input) {
        return null;
    }

    public AccumType mergeAccumulators(Iterable<AccumType> accums) {
        return null;
    }

    public outputType extractOutput(AccumType accum) {
        return null;
    }
}