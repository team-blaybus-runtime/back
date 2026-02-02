package com.init.global.argumentresolver;

import com.init.global.dto.CustomPageRequest;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Pageable} 파라미터를 커스텀 방식으로 파싱하여 {@link CustomPageRequest} 객체로 변환하는 ArgumentResolver입니다.
 * <p>
 * <b>사용 방법:</b>
 * <ul>
 *   <li>컨트롤러 메서드에서 {@code Pageable} 타입 파라미터를 선언하면 자동으로 동작합니다.</li>
 *   <li>파라미터는 쿼리스트링으로 전달합니다.</li>
 * </ul>
 * <p>
 * <b>지원 파라미터:</b>
 * <ul>
 *   <li><b>page</b>: 1부터 시작하는 페이지 번호 (예: {@code ?page=1})</li>
 *   <li><b>size</b>: 한 페이지에 조회할 데이터 개수 (예: {@code ?size=20})</li>
 *   <li><b>sort</b>: 정렬 필드와 방향 (예: {@code ?sort=createdAt,DESC&sort=name,ASC})</li>
 * </ul>
 * <p>
 * <b>예시:</b>
 * <pre>
 * {@code
 * // 컨트롤러 예시
 * @GetMapping("/users")
 * public ResponseEntity<List<UserResponse>> getUsers(Pageable pageable) {
 *     // pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort() 사용 가능
 * }
 * }
 * </pre>
 * <p>
 * <b>기본 동작:</b>
 * <ul>
 *   <li>page 파라미터가 없으면 1페이지(0 index)로 간주</li>
 *   <li>size 파라미터가 없으면 12로 기본값 적용</li>
 *   <li>sort 파라미터가 없으면 createdAt DESC로 정렬</li>
 *   <li>sort 방향 생략 시 DESC로 간주</li>
 * </ul>
 */
@Component
public class CustomPageableArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SIZE = "size";
    private static final String PARAM_SORT = "sort";
    private static final int DEFAULT_PAGE_SIZE = 12;
    private static final String DEFAULT_SORT_FIELD = "createdAt";
    private static final String DEFAULT_SORT_DIRECTION = "DESC";
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, DEFAULT_SORT_FIELD);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Pageable.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        int page = getPage(webRequest);
        int size = getSize(webRequest);
        Sort sort = getSort(webRequest);

        if (page < 0) {
            page = 0;
        }
        return new CustomPageRequest(page, size, sort);
    }

    private int getPage(NativeWebRequest webRequest) {
        String page = webRequest.getParameter(PARAM_PAGE);
        return page != null ? Integer.parseInt(page) - 1 : 0;
    }

    private int getSize(NativeWebRequest webRequest) {
        String size = webRequest.getParameter(PARAM_SIZE);
        return size != null ? Integer.parseInt(size) : DEFAULT_PAGE_SIZE;
    }

    private Sort getSort(NativeWebRequest webRequest) {
        String[] sortParams = webRequest.getParameterValues(PARAM_SORT);
        if (sortParams == null || sortParams.length == 0) {
            return DEFAULT_SORT;
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (String param : sortParams) {
            if (param == null || param.isBlank()) continue;

            String[] split = param.split(",");

            String direction = (split.length > 1) ? split[1] : DEFAULT_SORT_DIRECTION;
            Sort.Direction sortDirection = Sort.Direction.fromString(direction);

            String field = split[0];

            orders.add(new Sort.Order(sortDirection, field));
        }

        return Sort.by(orders);
    }
}
