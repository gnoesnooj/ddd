package com.ddd.oauth;

import com.ddd.user.domain.Role;
import com.ddd.user.domain.SocialType;
import com.ddd.user.domain.User;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String imageUrl;
    private SocialType socialType;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String imageUrl, SocialType socialType) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.socialType = socialType;
    }

    public static OAuthAttributes of(SocialType socialType, String userNameAttributeName, Map<String, Object> attributes){
        if(socialType == SocialType.KAKAO)
            return ofKakao(userNameAttributeName, attributes);
        else
            return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes){
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
        userNameAttributeName = "id";
        return OAuthAttributes.builder()
                .socialType(SocialType.KAKAO)
                .name((String) profile.get("nickname"))
                .email((String) kakao_account.get("email"))
                .imageUrl((String) profile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .socialType(SocialType.GOOGLE)
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .imageUrl((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity(SocialType socialType) {
        return User.builder()
                .socialType(socialType)
                .email(email)
                .nickname(name)
                .imageUrl(imageUrl)
                .role(Role.GUEST)
                .build();
    }
}
