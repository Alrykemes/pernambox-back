package com.dev.pernambox.controller;

import com.dev.pernambox.exceptions.UploadFilesException;
import com.dev.pernambox.service.MinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "File", description = "Operações relacionadas a Upload e Dowload/Vizualização dos arquivos")
public class FileController {

    private final MinioService minioService;

    @PostMapping("/upload")
    @Operation(summary = "Realiza upload de arquivos", description = "Upload de um ou mais arquivos de qualquer tipo, " +
            "imagem, pdf, docx, xlsx, entre outros")
    public ResponseEntity<String> upload(@RequestParam List<MultipartFile> files) throws Exception {
        List<String> fileNames = new ArrayList<>();
        files.forEach(file -> {
            try {
                minioService.uploadFile(file.getOriginalFilename(), file.getInputStream(), file.getContentType());
                fileNames.add(file.getOriginalFilename());
            } catch (Exception e) {
                throw new UploadFilesException("Erro ao fazer upload do arquivo " + file.getOriginalFilename());
            }
        });
        return fileNames.size() > 1 ?
                ResponseEntity.ok("Arquivos: \n" + fileNames + "\n salvos com sucesso!") :
                ResponseEntity.ok("Arquivo: " + fileNames.getFirst() + " salvo com sucesso!");
    }

    @GetMapping("/url/{nome}")
    @Operation(summary = "Gera presignedUrl de um arquivo", description = "Recebe nome do arquivo com sua extensão, e se" +
            " houver no repositório retorna url pré-assinada, válida por 10min")
    public ResponseEntity<String> gerarUrl(@PathVariable String nome) throws Exception {
        return ResponseEntity.ok(minioService.gerarUrlTemporaria(nome, (60 * 10))); // URL válida por 10min
    }
}
