
private static class MultiplyBy2AndFilter extends DoFn<Integer, Integer> {

    @ProcessElement
    public void processElement(ProcessContext context) {
        Integer value = context.element();
        context.output(value);
    }
}