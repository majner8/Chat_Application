package chatapp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class TextMessageDTO  extends MessageDTO{

	private String text;

	
}
