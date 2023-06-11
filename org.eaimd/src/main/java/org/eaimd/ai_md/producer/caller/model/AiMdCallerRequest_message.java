/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.producer.caller.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AiMdCallerRequest_message {

	String role;
	
	String content;
}
