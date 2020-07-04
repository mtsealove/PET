package com.vipet.petvip.Restful

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

// 반려동물 집밥원료 서비스가 xml 형식으로만 되어 있기 떄문에 별도의 파싱 클래스 생성
@Root(name = "response", strict = false)
class PublicData() {
    @field: Element(name = "body")
    var body: PublicBody? = null
}

@Root(name = "body", strict = false)
class PublicBody() {
    @field:Element(name = "items")
    var items: Items? = null
}

@Root(name = "items", strict = false)
class Items() {
    @field:ElementList(inline = true)
    var itemList: List<Item>? = null
}

@Root(name = "item", strict = false)
class Item() {
    @field:Element(name = "ashsQy", required = false)
    var ashsQy: String? = ""

    @field:Element(name = "clciQy", required = false)
    var clciQy: String? = ""

    @field:Element(name = "crbQy", required = false)
    var crbQy: String? = ""

    @field:Element(name = "crfbQy",  required = false)
    var crfbQy: String? = ""

    @field:Element(name = "dryMatter", required = false)
    var dryMatter: String? = ""

    @field:Element(name = "fatQy", required = false)
    var fatQy: String? = ""

    @field:Element(name = "feedClCode", required = false)
    var feedClCode: String? = ""

    @field:Element(name = "feedClCodeNm", required = false)
    var feedClCodeNm: String? = ""

    @field:Element(name = "feedNm", required = false)
    var feedNm: String? = ""

    @field:Element(name = "feedSeqNo", required = false)
    var feedSeqNo: String? = ""

    @field:Element(name = "liacQy", required = false)
    var liacQy: String? = ""

    @field:Element(name = "mitrQy", required = false)
    var mitrQy: String? = ""

    @field:Element(name = "mtralPc", required = false)
    var mtralPc: String? = ""

    @field:Element(name = "naQy", required = false)
    var naQy: String? = ""

    @field:Element(name = "originNm", required = false)
    var originNm: String? = ""

    @field:Element(name = "phphQy", required = false)
    var phphQy: String? = ""

    @field:Element(name = "protQy", required = false)
    var protQy: String? = ""

    @field:Element(name = "ptssQy", required = false)
    var ptssQy: String? = ""

    @field:Element(name = "slwtEdblfibrQy", required = false)
    var slwtEdblfibrQy: String? = ""

    @field:Element(name = "totEdblfibrQy", required = false)
    var totEdblfibrQy: String? = ""

    @field:Element(name = "trypQy", required = false)
    var trypQy: String? = ""

    @field:Element(name = "upperFeedClCode", required = false)
    var upperFeedClCode: String? = ""

    @field:Element(name = "vtmaQy", required = false)
    var vtmaQy: String? = ""
}
