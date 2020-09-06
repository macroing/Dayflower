/**
 * Provides the Wavefront Object API.
 * <p>
 * This API contains an {@link org.dayflower.wavefront.object.ObjectFileParser ObjectFileParser} that allows you to process Wavefront Object statements (in the form of {@link org.dayflower.wavefront.object.ObjectFileStatement ObjectFileStatement}s) as
 * they are observed. This should be memory-efficient. But this API also contains an {@link org.dayflower.wavefront.object.ObjectFile ObjectFile} that captures all such statements. This may be memory-inefficient, but it allows for greater flexibility.
 * <p>
 * To use the {@code ObjectFileParser}, consider the following example:
 * <pre>
 * <code>
 * try {
 *     ObjectFileParser objectFileParser = new ObjectFileParser();
 *     objectFileParser.addObjectFileParserObserver(objectFileStatement -&gt; System.out.println(objectFileStatement));
 *     objectFileParser.parse("path/to/file.obj");
 * } catch(ObjectFileException e) {
 *     e.printStackTrace();
 * }
 * </code>
 * </pre>
 * To use the {@code ObjectFile}, consider the following example:
 * <pre>
 * <code>
 * try {
 *     ObjectFile objectFile = ObjectFile.parse("path/to/file.obj");
 *     
 *     for(ObjectFileStatement objectFileStatement : objectFile) {
 *         System.out.println(objectFileStatement);
 *     }
 * } catch(ObjectFileException e) {
 *     e.printStackTrace();
 * }
 * </code>
 * </pre>
 */
package org.dayflower.wavefront.object;