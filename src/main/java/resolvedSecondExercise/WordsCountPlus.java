package resolvedExercice;

import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;

import java.util.Arrays;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.TextIO.Write;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.Create.Values;
import org.apache.beam.sdk.transforms.ParDo.SingleOutput;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.transforms.Combine.CombineFn;
import org.apache.beam.sdk.transforms.Combine.Globally;
import org.apache.beam.sdk.transforms.Combine.PerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordsCountPlus {

    static class SplitWords extends DoFn<String, Iterable<String>> {

        @ProcessElement
        public void processElement(ProcessContext context) {
            String line = context.element();
            context.output(Arrays.asList(line.split(" ")));        
        }
    }

    static class FilterWords extends DoFn<String, String> {

        @ProcessElement
        public void processElement(ProcessContext context) {
            String word = context.element();
            if(word.length() > 5){
                context.output(word);
            }
        }
    }

    static class Array2Elements extends DoFn<Iterable<String>, String> {

        @ProcessElement
        public void processElement(ProcessContext context) {
            Iterable<String> wordList = context.element();

            for(String word: wordList){
                context.output(word.trim().toLowerCase());
            }
        }
    }

    static class Word2KV extends DoFn<String, KV<String, Integer>> {

        @ProcessElement
        public void processElement(ProcessContext context) {
            String value = context.element();

            context.output(KV.of(value, 1));
        }
    }

    private static class CountWords extends CombineFn<Integer, Integer, Integer> {

        public Integer createAccumulator() {
            return 0;
        }
    
        public Integer addInput(Integer accum, Integer input) {
            return input + accum;
        }
    
        public Integer mergeAccumulators(Iterable<Integer> accums) {
            Integer sum = 0;

            for(Integer e: accums){
                sum += e;
            }

            return sum;
        }
    
        public Integer extractOutput(Integer accum) {
            return accum;
        }

    }

    

    public static void main(String[] args) {

        Pipeline p = Pipeline.create();

        p.apply(TextIO.read().from("data/corpus.txt"))
         .apply("Split words", ParDo.of(new SplitWords()))
         .apply("Flatten", Flatten.iterables())
         .apply("normalize", MapElements.into(TypeDescriptors.strings()).via((String word) -> word.trim().toLowerCase()))
         .apply("Filter words", Filter.by((String word) -> word.length() > 5))
         .apply(Count.perElement())
         .apply(MapElements.into(TypeDescriptors.strings()).via((KV<String, Long> pair) -> pair.getKey().toString()+" -> "+pair.getValue().toString()))
         .apply(TextIO.write().to("WordCountResult").withoutSharding());

         p.run().waitUntilFinish();

    }

}