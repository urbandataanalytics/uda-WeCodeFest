package example;

import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.TextIO.Write;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.Create.Values;
import org.apache.beam.sdk.transforms.ParDo.SingleOutput;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.transforms.Combine.CombineFn;
import org.apache.beam.sdk.transforms.Combine.Globally;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class step5 {

    private static class MultiplyBy2AndFilter extends DoFn<Integer, Integer> {

        @ProcessElement
        public void processElement(ProcessContext context) {
            Integer value = context.element();

            Integer new_value = value * 2;

            if (new_value < 200)
                context.output(new_value);
        }
    }

    private static class Int2String extends DoFn<Integer, String> {

        @ProcessElement
        public void processElement(ProcessContext context) {
            Integer value = context.element();
            context.output(value.toString());
        }

    }

    private static class AverageFn extends CombineFn<Integer, AverageFn.Accum, Integer> {
        @DefaultCoder(AvroCoder.class)
        public static class Accum {
            int sum = 0;
            int count = 0;
        }

        public Accum createAccumulator() {
            return new Accum();
        }

        public Accum addInput(Accum accum, Integer input) {
            accum.sum += input;
            accum.count++;
            return accum;
        }

        public Accum mergeAccumulators(Iterable<Accum> accums) {
            Accum merged = createAccumulator();
            for (Accum accum : accums) {
                merged.sum += accum.sum;
                merged.count += accum.count;
            }
            return merged;
        }

        public Integer extractOutput(Accum accum) {
            return ((Integer) accum.sum) / accum.count;
        }
    }

    public static void main(String[] args) {

        Pipeline p = Pipeline.create();

        // Step 1
        Values<Integer> values = Create.of(20, 60, 80, 120, 50);

        PCollection<Integer> output1 = p.apply(values);

        // Step 2
        DoFn<Integer, Integer> multiplyBy2AndFilterFunction = new MultiplyBy2AndFilter();

        SingleOutput<Integer, Integer> transformAndFilter = ParDo.of(multiplyBy2AndFilterFunction);

        PCollection<Integer> filteredOutput = output1.apply(transformAndFilter);

        // Step 3
        AverageFn combiner = new AverageFn();

        Globally<Integer, Integer> combineTransform = Combine.globally(combiner).withoutDefaults();

        PCollection<Integer> combinedOutput = filteredOutput.apply(combineTransform);

        // Step 4
        DoFn<Integer, String> int2StringFunction = new Int2String();

        SingleOutput<Integer, String> castTransform = ParDo.of(int2StringFunction);

        PCollection<String> string_output = combinedOutput.apply(castTransform);

        // Step 5
        Write writeTransform = TextIO.write().to("myNumbers");

        string_output.apply(writeTransform);

        p.run().waitUntilFinish();
    
    
    }

}