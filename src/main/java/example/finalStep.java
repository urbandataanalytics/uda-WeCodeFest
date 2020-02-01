package example;

import org.apache.beam.sdk.values.KV;
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
import org.apache.beam.sdk.transforms.Combine.PerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class finalStep {

    private static class MultiplyBy2AndFilter extends DoFn<Integer, Integer> {

        @ProcessElement
        public void processElement(ProcessContext context) {
            Integer value = context.element();

            Integer new_value = value * 2;

            if (new_value < 200)
                context.output(new_value);
        }
    }

    private static class Value2Pair extends DoFn<Integer, KV<Boolean, Integer>> {

        @ProcessElement
        public void processElement(ProcessContext context) {
            Integer value = context.element();

            KV<Boolean, Integer> new_value = KV.of(value > 100, value);
            context.output(new_value);
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

        p.apply(Create.of(20, 60, 80, 120, 50))
         .apply(ParDo.of(new MultiplyBy2AndFilter()))
         .apply(ParDo.of(new Value2Pair()))
         .apply(Combine.<Boolean, Integer, Integer>perKey(new AverageFn()))
         .apply(MapElements
                    .into(TypeDescriptors.strings())
                    .via((KV<Boolean, Integer> input) -> input.getKey().toString()+" -> "+input.getValue().toString())
                )
        .apply(ParDo.of(new libs.utils.printElements()));
/*          .apply(TextIO.write().to("myNumbers"));
 */
        p.run().waitUntilFinish();
    
    
    }

}