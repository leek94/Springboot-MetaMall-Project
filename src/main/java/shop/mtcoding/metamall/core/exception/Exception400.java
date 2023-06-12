package shop.mtcoding.metamall.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import shop.mtcoding.metamall.dto.ResponseDTO;
import shop.mtcoding.metamall.dto.ValidDTO;

//  username = "aslkdjlasdkfasldkfjasldf"
// "username": "유저네임이 너무 길어요"

// 유효성 실패, 잘못된 파라메타 요청
@Getter
public class Exception400 extends RuntimeException {
    private String key;
    private String vlaue;

    public Exception400(String key, String vlaue) {
        super(vlaue);
        this.key = key;
        this.vlaue = vlaue;
    }

    public Exception400(String message) {
        super(message);
    }

    public ResponseDTO<?> body(){
        ResponseDTO<ValidDTO> responseDto = new ResponseDTO<>();
        ValidDTO validDTO = new ValidDTO(key, vlaue);

        responseDto.fail(HttpStatus.BAD_REQUEST, "badRequest", validDTO);
        return responseDto;
    }

    public HttpStatus status(){
        return HttpStatus.BAD_REQUEST;
    }
}