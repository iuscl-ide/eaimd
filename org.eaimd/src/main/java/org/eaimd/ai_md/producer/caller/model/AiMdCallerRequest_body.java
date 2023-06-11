/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.producer.caller.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AiMdCallerRequest_body {

	String model;
	
	List<AiMdCallerRequest_message> messages = new ArrayList<>();
}
