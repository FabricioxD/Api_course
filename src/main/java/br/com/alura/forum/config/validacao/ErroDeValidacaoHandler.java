package br.com.alura.forum.config.validacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

// com esse annotation o handler intercepta as exceções que ocorrem em métodos que têm a anotação de @responseBody
// criando um handler de erro, para tratar exceções em métodos post, por exemplo.
@RestControllerAdvice
public class ErroDeValidacaoHandler {

    // message source é utilizado para tratar a mensagem de erro da exceção no idioma do usuário
    @Autowired
    private MessageSource messageSource;
    // código de erro que será retornado
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    // tipo de exceção que vai ser tratada.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroFormularioDto> handle(MethodArgumentNotValidException argumentNotValidException) {
        List<ErroFormularioDto> erroFormularioDtos = new ArrayList<>();

        List<FieldError> errorList = argumentNotValidException.getBindingResult().getFieldErrors();

        errorList.forEach(e -> {
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroFormularioDto dto = new ErroFormularioDto(e.getField(), mensagem);
            erroFormularioDtos.add(dto);
        });

        return erroFormularioDtos;
    }
}
