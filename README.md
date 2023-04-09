# 💰 Gagyebu, 가계부
> 나의 재정 상태를 보기 쉬운 ui로 전체적인 통계를 내릴 수 있는 앱 입니다. 



<br/>

## 🧞‍♀️ 프로젝트 참여자

|[![](https://github.com/H-Zoon.png?size=100)](https://github.com/H-Zoon) |[![](https://github.com/eunjjungg.png?size=100)](https://github.com/eunjjungg) |
|:---:|:---:|
| **HyunJoon Choi** | **EunJung Jung** | 

<br/><br/>

## 📌 [@eunjjungg](https://github.com/eunjjungg) : Native 기술 스택

- Android Studio, Kotlin
- MVVM
- Live Data, Data Binding
- Android Jetpack (Compose UI, Android KTX)
- Custom View
- Room
- Dark Mode Theme
- 협업 - Jira, Gitflow, Bitbucket, Confluence, Figma

<br/><br/>

## 💻 [@eunjjungg](https://github.com/eunjjungg) : Native 담당 부분

- 프로젝트 세팅
- 맡은 구현 화면 디자인
- 연간 통계 관련 로직
- 월간 통계 관련 로직
- 월간 상세 소비 내역 관련 로직


<br/><br/>

## 🌱 [@eunjjungg](https://github.com/eunjjungg) : 구현 화면 및 설명

### 🙋‍♀️ 연간 통계
https://user-images.githubusercontent.com/100047095/230774726-b533c998-eef9-4c7d-9d84-62e019f5f909.mp4

- 막대 그래프가 그려지는 모습은 애니메이션으로 구현
- Local DB(Room)에서 데이터를 불러와 해당 수치로 그래프 그림
- 하단 리포트의 각 사각 컴포넌트들은 xml 없이 코드로 작성한 Custom View 사용

<br/>

### 🙋‍♀️ 월간 통계
https://user-images.githubusercontent.com/100047095/230774889-b2b92ffa-3254-40ab-ad16-c8e5373b0afe.mp4

- XML로 구현한 파이 차트
- Local DB(Room)에서 데이터를 불러와 해당 수치로 그래프 그림
- 파이 차트가 그려지는 모습은 애니메이션으로 구현

<br/>

### 🙋‍♀️ 월간 상세 리포트
https://user-images.githubusercontent.com/100047095/230774899-295777fc-2491-44d7-863e-509644954c8a.mp4

- 각 `fragment` 모두 같은 `viewmodel` 사용하도록 구현
- 마지막 다음으로 넘어가기 버튼 클릭 시 회원가입 절차 완료되고 서버로 가입 요청하도록 구현
- 가입 도중 끊긴다면 카카오 로그인에서 회원 삭제하도록 구현



<br/><br/>

## 🔫 **Trouble Shooting**

```
1️⃣ 이슈 관리
```
Gagyebu를 개발하기 이전에 진행했던 프로젝트는 네이티브 팀원이 저 혼자였거나 이슈 관리에 대한 지식 없이 진행했었습니다. 
하지만 Gagyebu 프로젝트는 협업자와의 커뮤니케이션이 중요하다고 느끼게 되었고 이를 해결하기 위해 Jira를 통한 이슈 관리를 진행했습니다. 
이 방법을 사용하며 느낀점은 상대방이 현재 무엇을 진행중인지 확실히 알 수 있는 것도 장점이지만 내 개발 현황이 어떤지 또한 파악할 수 있다는 점이 저에게는 크게 와닿았습니다. 
그리고 이슈 관리를 하며 가장 크게 배운 점은 스프린트 단위였던 한 주 단위로 스크럼을 진행하며 특정 기능을 구현하기 위해 
**내가 얼마만큼의 시간이 필요할지 예측하고 회고하는 습관**을 갖게 되었습니다.



```
2️⃣ 애니메이션
```
주어진 수치에 맞춰 뷰를 그리는 것은 이전에도 해봤던 경험이 있지만 뷰를 그리는 과정을 애니메이션으로 표현하는 것은 Gagyebu를 진행하며 처음 해봤던 과정이었습니다. 저는 주어진 수치에 맞춰 **파이 그래프**를 그리는 과정을 수행했습니다. 이 과정에서 애니메이션이라는 것을 처음 접해보았고 각 수치에 맞춰 보정을 해주는 것이 쉽지는 않았지만 실제로 그려지는 모습을 보니 유저 입장에서 애니메이션이 있는 편이 그래프를 이해하는데 도움을 준다는 것을 알게 되었습니다. 또한 수치 보정을 꼼꼼히 해주어 원하던 대로 동작하는 애니메이션을 볼 때  느낄 수 있었습니다.



```
3️⃣ Compose에서의 event handling
```
저는 Jetpack Compose UI를 이번 프로젝트를 통해 처음 사용해보았습니다. 이전에 google에서 진행한 ComposeCamp2022는 수료했지만 실제로 프로젝트에 사용해 본 것은 처음이었습니다. Compose에서 중요하게 생각하는 개념인 `state hoisting` 또한 처음 접하는 주제라 상태가 변했을 때 이벤트 핸들링을 어디서 해주어야 옳을지 많은 고민을 해보았습니다.



```
4️⃣ Refactoring
```
늘 제 코드의 부족함을 어느정도 인지하고 있고 프로젝트가 끝날 때 즈음 아쉬운 부분에 대해 기록은 해두었지만 실제로 refactoring 과정을 수행해본적은 없었습니다. 하지만 Gagyebu 프로젝트를 1차 구현 완료했을 때 `coroutine`을 잘못 사용하고 있는 부분을 발견했고 원형 그래프를 compose로도 구현해보고 싶어 migration하는 refactoring 과정을 진행했습니다. compose로 migration하는 과정에서는 compose에 대한 이해도를 조금 더 높일 수 있는 좋은 기회가 되었지만 저는 `coroutine`을 리팩토링 하면서 가장 크게 배울 수 있었습니다. 제가 이전까지 작성했던 코드는 `coroutine`의 비동기성을 제대로 활용하지 못하고 있었고 이를 고치며 `coroutine`에 대한 이해도 또한 높일 수 있었습니다. 물론 제가 고친 후의 코드도 현업에 계신 junior, senior 개발자 분들이 보셨을 때 부족함이 많은 코드이겠지만 이 과정을 계속 거친다면 저는 더 좋은 코드를 작성할 수 있을 것이라고 생각합니다.

