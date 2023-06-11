/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.producer.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AiMdReferenceHeader {

	String id;
	
	String textAction;
}
