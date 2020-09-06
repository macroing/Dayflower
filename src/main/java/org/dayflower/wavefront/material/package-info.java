/**
 * Provides the Wavefront Material API.
 * <p>
 * This API contains a {@link org.dayflower.wavefront.material.MaterialFileParser MaterialFileParser} that allows you to process Wavefront Material statements (in the form of
 * {@link org.dayflower.wavefront.material.MaterialFileStatement MaterialFileStatement}s) as they are observed. This should be memory-efficient. But this API also contains a {@link org.dayflower.wavefront.material.MaterialFile MaterialFile} that
 * captures all such statements. This may be memory-inefficient, but it allows for greater flexibility.
 * <p>
 * To use the {@code MaterialFileParser}, consider the following example:
 * <pre>
 * <code>
 * try {
 *     MaterialFileParser materialFileParser = new MaterialFileParser();
 *     materialFileParser.addMaterialFileParserObserver(materialFileStatement -&gt; System.out.println(materialFileStatement));
 *     materialFileParser.parse("path/to/file.mtl");
 * } catch(MaterialFileException e) {
 *     e.printStackTrace();
 * }
 * </code>
 * </pre>
 * To use the {@code MaterialFile}, consider the following example:
 * <pre>
 * <code>
 * try {
 *     MaterialFile materialFile = MaterialFile.parse("path/to/file.mtl");
 *     
 *     for(MaterialFileStatement materialFileStatement : materialFile) {
 *         System.out.println(materialFileStatement);
 *     }
 * } catch(MaterialFileException e) {
 *     e.printStackTrace();
 * }
 * </code>
 * </pre>
 */
package org.dayflower.wavefront.material;