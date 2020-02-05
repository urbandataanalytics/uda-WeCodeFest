
private static class ExampleDoFn extends DoFn<Integer, Integer> {

    @ProcessElement
    public void processElement(ProcessContext context) {
        Integer value = context.element();
        context.output(value);
    }
}