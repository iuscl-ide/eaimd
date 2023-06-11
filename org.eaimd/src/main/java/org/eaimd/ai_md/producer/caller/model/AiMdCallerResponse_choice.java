/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.producer.caller.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AiMdCallerResponse_choice {

	AiMdCallerResponse_message message;
	
	String finish_reason;
	
	int index;
}
