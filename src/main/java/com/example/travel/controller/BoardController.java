package com.example.travel.controller;

import com.example.travel.dto.BoardRequestDto;
import com.example.travel.dto.BoardResponseDto;
import com.example.travel.dto.MemberLoginRequest;
import com.example.travel.entity.Member;
import com.example.travel.repository.BoardRepository;
import com.example.travel.repository.MemberRepository;
import com.example.travel.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"게시판 서비스"}, description = "게시글 서비스를 담당합니다.")
public class BoardController {

    private final BoardService boardService;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    /**
     * 게시글 생성
     */
    @PostMapping("/board/insert")
    @ApiOperation(value = "게시글 생성", notes = "새 게시글을 생성합니다.")
    public Long save(@RequestBody final BoardRequestDto params) {
        return boardService.save(params);
    }

    /**
     * 게시글 리스트 조회
     */
    @GetMapping("/board/selectAll")
    @ApiOperation(value = "게시글 전체 조회", notes = "전체 게시글 내용을 가져옵니다.")
    public List<BoardResponseDto> findAll() {
        return boardService.findAll();
    }

    /**
     * 게시글 수정
     */
    @PatchMapping("/board/{id}")
    @ApiOperation(value = "게시글 수정", notes = "상세 게시글 내용을 수정합니다.")
    public Long save(@PathVariable final Long id, @RequestBody final BoardRequestDto params) {
        return boardService.update(id, params);
    }

    /**
     * 상세 게시글 내용 가져오기
     */
    @GetMapping("/board/{id}")
    @ApiOperation(value = "게시글 조회", notes = "상세 게시글 내용을 가져옵니다.")
    public BoardResponseDto findByBoardId(@PathVariable final Long id){
        return boardService.findByBoardId(id);
    }

    /**
     * 게시글 삭제
     */
    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제합니다.")
    @DeleteMapping("/board/{id}")
    public void deleteBoardById(@PathVariable final Long id){
        boardService.deleteById(id);
    }

    /**
     * 게시글 좋아요
     */
    @PostMapping("/board/{id}")
    @ApiOperation(value = "게시글 좋아요", notes = "사용자가 게시글 좋아요를 누릅니다.")
    public String thumbsUp(@PathVariable Long id, Authentication authentication) {
        MemberLoginRequest memberLoginRequest = (MemberLoginRequest) authentication.getPrincipal();
        String strMemberId = memberLoginRequest.getId();
        return boardService.thumbsUp(id, strMemberId);
    }

    /**
     * 게시글 최신 순으로 가져오기
     * ?order = -createdDate : 내림차순 desc created_date
     *
     * 게시글 오래된 순으로 가져오기
     * ?order = createdDate : 오름차순 asc create_date
     */
    @GetMapping("/boards")
    @ApiOperation(value = "게시글 정렬", notes = "default 는 최신 순으로 게시글을 가져옵니다. order = createdDate 인 경우 오래된 순으로 가져옵니다.")
    public List<BoardResponseDto> findByCreatedDateAsc(@RequestParam (required = false, defaultValue = "-createdDate") @Nullable String order,
                                                       @RequestParam(required = false, defaultValue = "1") int page){
        if(order.equals("-createdDate") || order.equals("")){
            return boardService.findByCreatedDateDesc(page-1);
        }
        return boardService.findByCreatedDateAsc(page-1);
    }
}
