package resolvedFirstExercice;

import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.TextIO.Write;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.Create.Values;
import org.apache.beam.sdk.transforms.ParDo.SingleOutput;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class step3 {

  private static class Int2String extends DoFn<Integer, String> {

    @ProcessElement
    public void processElement(ProcessContext context) {
      Integer value = context.element();
      context.output(value.toString());
    }

  }

  public static void main(String[] args) {

    // Step: Create Pipeline
    Pipeline p = Pipeline.create();

    // Step: Read Data
    Values<Integer> values = Create.of(20, 60, 80, 120, 50);

    PCollection<Integer> output1 = p.apply(values);

    // Step: Multiply and Filter

    // Step: get KV

    // Step: Get average

    // Step: Value to String
    DoFn<Integer, String> int2StringFunction = new Int2String();

    SingleOutput<Integer, String> castTransform = ParDo.of(int2StringFunction);
  
    PCollection<String> string_output = output1.apply(castTransform);

    // Step: Write Data
    Write writeTransform = TextIO.write().to("myNumbers");

    string_output.apply(writeTransform);

    // Step: Execute Pipeline
    p.run().waitUntilFinish();

  }

}