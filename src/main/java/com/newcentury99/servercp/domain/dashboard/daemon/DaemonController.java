package com.newcentury99.servercp.domain.dashboard.daemon;

import com.newcentury99.servercp.domain.dashboard.daemon.dto.*;
import com.newcentury99.servercp.domain.dashboard.daemon.service.DaemonCrudService;
import com.newcentury99.servercp.domain.dashboard.daemon.service.DaemonManageService;
import com.newcentury99.servercp.global.dtometadata.DtoMataData;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@RestController
public class DaemonController {
    private final DaemonCrudService daemonCrudService;
    private final DaemonManageService daemonManageService;

    @PostMapping("/daemon")
    public ResponseEntity<?> createDaemon(@Valid @RequestBody CreateDaemonReqDto reqDto) {
        DtoMataData dtoMataData;
        try {
            daemonCrudService.createDaemon(reqDto);
            dtoMataData = new DtoMataData(true, "create daemon success");
            return ResponseEntity.ok(new CreateDaemonResDto(dtoMataData));
        } catch (Exception e) {
            dtoMataData = new DtoMataData(false, e.getMessage());
            return ResponseEntity.status(400).body(new CreateDaemonResDto(dtoMataData));
        }
    }
    @PutMapping("/daemon")
    public ResponseEntity<?> updateDaemon(@Valid @RequestBody UpdateDaemonReqDto reqDto) {
        DtoMataData dtoMataData;
        try {
            daemonCrudService.updateDaemon(reqDto);
            dtoMataData = new DtoMataData(true, "update daemon success");
            return ResponseEntity.ok(new UpdateDaemonResDto(dtoMataData));
        } catch (Exception e) {
            dtoMataData = new DtoMataData(false, e.getMessage());
            return ResponseEntity.status(400).body(new UpdateDaemonResDto(dtoMataData));
        }
    }
    @DeleteMapping("/daemon")
    public ResponseEntity<?> deleteDaemon(@Valid @RequestBody DeleteDaemonReqDto reqDto) {
        DtoMataData dtoMataData;
        try {
            dtoMataData = new DtoMataData(true, "delete daemon success");
            daemonCrudService.deleteDaemon(reqDto);
            return ResponseEntity.ok(new DeleteDaemonResDto(dtoMataData));
        } catch (Exception e) {
            dtoMataData = new DtoMataData(false, e.getMessage());
            return ResponseEntity.status(400).body(new DeleteDaemonResDto(dtoMataData));
        }
    }

    @GetMapping("/daemon/log")
    public ResponseEntity<?> getDaemonLog(@RequestParam(name = "id") Long daemonId) {
        DtoMataData dtoMataData;
        try {
            dtoMataData = new DtoMataData(true, "successfully get daemon log");
            return ResponseEntity.ok(new GetDaemonLogResDto(
                    dtoMataData, daemonManageService.getDaemonLogById(daemonId)
            ));
        } catch (Exception e) {
            dtoMataData = new DtoMataData(false, e.getMessage());
            return ResponseEntity.status(400).body(new GetDaemonLogResDto(dtoMataData));
        }
    }

    @GetMapping("/daemon/logfile")
    public ResponseEntity<?> getDaemonLogFile(@RequestParam(name = "id") Long daemonId) {
        DtoMataData dtoMataData;
        String daemonLog;
        try {
            daemonLog = daemonManageService.getDaemonLogById(daemonId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=temp_daemon_log.txt")
                    .contentLength(daemonLog.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(
                            new ByteArrayInputStream(daemonLog.getBytes(StandardCharsets.UTF_8))
                    ));
        } catch (Exception e) {
            dtoMataData = new DtoMataData(false, e.getMessage());
            return ResponseEntity.status(400).body(new GetDaemonLogResDto(dtoMataData));
        }
    }

    @GetMapping("/daemon/status")
    public ResponseEntity<?> getDaemonStatus(@RequestParam(name = "id") Long daemonId) {
        DtoMataData dtoMataData;
        try {
            dtoMataData = new DtoMataData(true, "successfully get daemon status");
            return ResponseEntity.ok(new GetDaemonStatusResDto(
                    dtoMataData, daemonManageService.getDaemonStatus(daemonId)
            ));
        } catch (Exception e) {
            dtoMataData = new DtoMataData(false, e.getMessage());
            return ResponseEntity.status(400).body(new GetDaemonStatusResDto(dtoMataData));
        }
    }

    @PostMapping("/daemon/start")
    public ResponseEntity<?> startDaemon(@Valid @RequestBody StartDaemonReqDto reqDto) {
        DtoMataData dtoMataData;
        try {
            daemonManageService.startDaemon(reqDto.getId());
            dtoMataData = new DtoMataData(true, "daemon start success");
            return ResponseEntity.ok(new StartDaemonResDto(dtoMataData));
        } catch (Exception e) {
            dtoMataData = new DtoMataData(false, e.getMessage());
            return ResponseEntity.status(400).body(new StartDaemonResDto(dtoMataData));
        }
    }

    @PostMapping("/daemon/stop")
    public ResponseEntity<?> stopDaemon(@Valid @RequestBody StopDaemonReqDto reqDto) {
        DtoMataData dtoMataData;
        try {
            daemonManageService.stopDaemon(reqDto.getId());
            dtoMataData = new DtoMataData(true, "daemon stop success");
            return ResponseEntity.ok(new StopDaemonResDto(dtoMataData));
        } catch (Exception e) {
            dtoMataData = new DtoMataData(false, e.getMessage());
            return ResponseEntity.status(400).body(new StopDaemonResDto(dtoMataData));
        }
    }

    @PostMapping("/daemon/restart")
    public ResponseEntity<?> restartDaemon(@Valid @RequestBody RestartDaemonReqDto reqDto) {
        DtoMataData dtoMataData;
        try {
            daemonManageService.restartDaemon(reqDto.getId());
            dtoMataData = new DtoMataData(true, "daemon restart success");
            return ResponseEntity.ok(new RestartDaemonResDto(dtoMataData));
        } catch (Exception e) {
            dtoMataData = new DtoMataData(false, e.getMessage());
            return ResponseEntity.status(400).body(new RestartDaemonResDto(dtoMataData));
        }
    }

    @PostMapping("/daemon/upgrade")
    public ResponseEntity<?> upgradeDaemon(
            @RequestPart(value = "json")UpgradeDaemonReqDto reqDto,
            @RequestPart(value = "file")MultipartFile uploadedBinaryFile
    ) {
        try {
            daemonManageService.upgradeDaemon(reqDto, uploadedBinaryFile);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
