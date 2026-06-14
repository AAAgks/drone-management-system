package com.drone.service.impl;

import com.drone.common.Constants;
import com.drone.domain.dto.DroneDTO;
import com.drone.domain.entity.Drone;
import com.drone.service.AiAttributeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * AI 属性生成服务实现 —— 等级制策略。
 * <p>
 * 根据无人机名称和描述中的关键词，自动匹配等级（消费级/工业级/军用级），
 * 在对应等级的参数范围内随机生成合理的技术指标。
 * <p>
 * 例如：含"军用"关键词的无人机会得到更大的航程、载重和速度范围，
 * 含"巡检"、"测绘"的则为工业级参数。
 */
@Service
public class AiAttributeServiceImpl implements AiAttributeService {

    private static final Logger log = LoggerFactory.getLogger(AiAttributeServiceImpl.class);
    private static final Random RANDOM = new Random();

    /** 制造商候选列表 */
    private static final String[] MANUFACTURERS = {"大疆创新", "极飞科技", "亿航智能", "道通智能",
            "智迪科技", "飞米科技", "纵横股份", "科比特航空", "Skydio", "Parrot", "中航工业", "航天彩虹"};

    /**
     * 生成无人机属性。
     * <p>
     * 流程：检测等级 → 在等级范围内随机取值 → 组装实体 → 转为 DTO。
     */
    @Override
    public DroneDTO generateAttributes(String name, String description) {
        log.info("AI 属性生成：name={}, desc={}", name, description);
        DroneGrade grade = detectGrade(name, description);

        Drone drone = Drone.builder()
                .name(name)
                .model(name + " " + grade.label)
                .serialNumber(generateSerial())
                .manufacturer(pick(MANUFACTURERS))
                .type(grade.type())
                .weight(round2(rand(grade.minWeight(), grade.maxWeight())))
                .maxSpeed(round1(rand(grade.minSpeed(), grade.maxSpeed())))
                .maxAltitude(round1(rand(grade.minAltitude(), grade.maxAltitude())))
                .endurance(round1(rand(grade.minEndurance(), grade.maxEndurance())))
                .rangeDistance(round1(rand(grade.minRange(), grade.maxRange())))
                .maxPayload(round2(rand(grade.minPayload(), grade.maxPayload())))
                .cameraType(pick(grade.cameras()))
                .batteryCapacity((int) Math.round(rand(grade.minBattery(), grade.maxBattery())))
                .gpsAccuracy(round2(rand(grade.minGps(), grade.maxGps())))
                .operatingTemperature(grade.operatingTemp())
                .waterproofLevel(grade.waterproof())
                .status(Constants.STATUS_NORMAL)
                .aiGenerated(Constants.AI_GENERATED)
                .description(description != null ? description : "AI 等级制自动生成：" + grade.label)
                .purchaseDate(LocalDateTime.now())
                .build();
        return DroneDTO.fromEntity(drone);
    }

    /**
     * 关键词等级检测。
     * <p>
     * 优先级：军用 > 工业 > 消费级（默认）。
     */
    private DroneGrade detectGrade(String name, String description) {
        String text = name + " " + (description != null ? description : "");
        if (containsAny(text, "军用", "military", "combat", "战斗", "攻击", "侦察打击", "察打一体", "制导", "电子战")) {
            return DroneGrade.MILITARY;
        }
        if (containsAny(text, "工业", "industrial", "巡检", "测绘", "植保", "喷洒", "物流", "消防", "救援", "警用")) {
            return DroneGrade.INDUSTRIAL;
        }
        if (containsAny(text, "固定翼", "直升机", "垂直起降", "vtol")) {
            return DroneGrade.INDUSTRIAL;
        }
        return DroneGrade.CONSUMER; // 默认消费级
    }

    /** 检查文本是否包含任意关键词 */
    private boolean containsAny(String text, String... keywords) {
        for (String kw : keywords) {
            if (text.contains(kw)) return true;
        }
        return false;
    }

    /** 在 [min, max) 范围内生成随机数 */
    private double rand(double min, double max) { return min + RANDOM.nextDouble() * (max - min); }
    /** 保留 1 位小数 */
    private double round1(double v) { return Math.round(v * 10.0) / 10.0; }
    /** 保留 2 位小数 */
    private double round2(double v) { return Math.round(v * 100.0) / 100.0; }
    /** 从数组中随机选取一个元素 */
    private String pick(String[] arr) { return arr[RANDOM.nextInt(arr.length)]; }

    /** 生成序列号，格式：UAV-YYYYMMDD-xxxx */
    private String generateSerial() {
        return String.format("UAV-%tY%<tm%<td-%04d", new java.util.Date(), RANDOM.nextInt(9000) + 1000);
    }

    /**
     * 无人机等级枚举。
     * <p>
     * 每个等级定义了各项技术参数的最小/最大值、默认类型、防水等级、相机选项等。
     * AI 生成时在对应范围内随机取值。
     */
    private enum DroneGrade {
        /** 消费级：多旋翼，轻便短航程 */
        CONSUMER("消费级", "多旋翼",
                0.2, 2.0,
                30.0, 80.0,
                100.0, 8000.0,
                15.0, 50.0,
                3.0, 15.0,
                0.1, 1.5,
                2000.0, 8000.0,
                0.5, 2.5,
                "-10℃~40℃", "IP43",
                "4K CMOS", "4K/30fps", "12MP 广角"),

        /** 工业级：多旋翼，中大载荷长航时 */
        INDUSTRIAL("工业级", "多旋翼",
                5.0, 50.0,
                40.0, 120.0,
                2000.0, 8000.0,
                40.0, 120.0,
                2.0, 20.0,
                5.0, 30.0,
                8000.0, 30000.0,
                0.3, 2.0,
                "-20℃~55℃", "IP55",
                "4K/60fps HDR", "20MP 1英寸CMOS", "红外热成像", "双光云台", "30倍变焦"),

        /** 军用级：固定翼，大载荷超远程 */
        MILITARY("军用级", "固定翼",
                30.0, 500.0,
                80.0, 500.0,
                5000.0, 20000.0,
                120.0, 720.0,
                10.0, 300.0,
                30.0, 200.0,
                20000.0, 80000.0,
                0.1, 1.0,
                "-40℃~70℃", "IP67",
                "光电吊舱", "多光谱/SAR雷达", "激光制导吊舱", "电子战吊舱");

        final String label;
        final String typeVal;
        final double minWeight, maxWeight;
        final double minSpeed, maxSpeed;
        final double minAltitude, maxAltitude;
        final double minEndurance, maxEndurance;
        final double minRange, maxRange;
        final double minPayload, maxPayload;
        final double minBattery, maxBattery;
        final double minGps, maxGps;
        final String operatingTemp;
        final String waterproof;
        final String[] cameras;

        DroneGrade(String label, String typeVal,
                   double minWeight, double maxWeight,
                   double minSpeed, double maxSpeed,
                   double minAltitude, double maxAltitude,
                   double minEndurance, double maxEndurance,
                   double minRange, double maxRange,
                   double minPayload, double maxPayload,
                   double minBattery, double maxBattery,
                   double minGps, double maxGps,
                   String operatingTemp, String waterproof,
                   String... cameras) {
            this.label = label; this.typeVal = typeVal;
            this.minWeight = minWeight; this.maxWeight = maxWeight;
            this.minSpeed = minSpeed; this.maxSpeed = maxSpeed;
            this.minAltitude = minAltitude; this.maxAltitude = maxAltitude;
            this.minEndurance = minEndurance; this.maxEndurance = maxEndurance;
            this.minRange = minRange; this.maxRange = maxRange;
            this.minPayload = minPayload; this.maxPayload = maxPayload;
            this.minBattery = minBattery; this.maxBattery = maxBattery;
            this.minGps = minGps; this.maxGps = maxGps;
            this.operatingTemp = operatingTemp; this.waterproof = waterproof;
            this.cameras = cameras;
        }

        String type() { return typeVal; }
        String[] cameras() { return cameras; }
        double minWeight() { return minWeight; } double maxWeight() { return maxWeight; }
        double minSpeed() { return minSpeed; } double maxSpeed() { return maxSpeed; }
        double minAltitude() { return minAltitude; } double maxAltitude() { return maxAltitude; }
        double minEndurance() { return minEndurance; } double maxEndurance() { return maxEndurance; }
        double minRange() { return minRange; } double maxRange() { return maxRange; }
        double minPayload() { return minPayload; } double maxPayload() { return maxPayload; }
        double minBattery() { return minBattery; } double maxBattery() { return maxBattery; }
        double minGps() { return minGps; } double maxGps() { return maxGps; }
        String operatingTemp() { return operatingTemp; }
        String waterproof() { return waterproof; }
    }
}
