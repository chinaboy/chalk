/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package chalk.tools.formats.ad;

import chalk.tools.cmdline.ArgumentParser;
import chalk.tools.cmdline.StreamFactoryRegistry;
import chalk.tools.cmdline.params.DetokenizerParameter;
import chalk.tools.formats.DetokenizerSampleStreamFactory;
import chalk.tools.formats.convert.NameToTokenSampleStream;
import chalk.tools.namefind.NameSample;
import chalk.tools.tokenize.TokenSample;
import chalk.tools.util.ObjectStream;

/**
 * <b>Note:</b> Do not use this class, internal use only!
 */
public class ADTokenSampleStreamFactory extends
    DetokenizerSampleStreamFactory<TokenSample> {

  interface Parameters extends ADNameSampleStreamFactory.Parameters,
      DetokenizerParameter {
  }

  public static void registerFactory() {
    StreamFactoryRegistry.registerFactory(TokenSample.class, "ad",
        new ADTokenSampleStreamFactory(Parameters.class));
  }

  protected <P> ADTokenSampleStreamFactory(Class<P> params) {
    super(params);
  }

  public ObjectStream<TokenSample> create(String[] args) {
    Parameters params = ArgumentParser.parse(args, Parameters.class);
    language = params.getLang();

    ObjectStream<NameSample> samples = StreamFactoryRegistry.getFactory(
        NameSample.class, "ad")
        .create(
            ArgumentParser.filter(args,
                ADNameSampleStreamFactory.Parameters.class));
    return new NameToTokenSampleStream(createDetokenizer(params), samples);
  }
}
