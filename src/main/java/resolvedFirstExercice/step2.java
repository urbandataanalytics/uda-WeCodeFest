package resolvedFirstExercice;

import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.Create.Values;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class step2 {

  public static void main(String[] args) {


    // Step: Create Pipeline
    Pipeline p = Pipeline.create();

    // Step: Read Data
    Values<Integer> values = Create.of(20, 60, 80, 120, 50);
    PCollection<Integer> input = p.apply(values);

    // Step: Multiply and Filter

    // Step: get KV

    // Step: Get average

    // Step: Value to String

    // Step: Write Data

    // Step: Execute Pipeline

    // Step: Read Data

    // Step: Multiply and Filter

    // Step: get KV

    // Step: Get average

    // Step: Value to String

    // Step: Write Data

    // Step: Execute Pipeline
    p.run().waitUntilFinish();

  }

}