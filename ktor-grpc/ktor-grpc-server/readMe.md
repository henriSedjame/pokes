
## Add following dependencies

        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-netty</artifactId>
            <version>${grpc_version}</version>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-protobuf</artifactId>
            <version>${grpc_version}</version>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-stub</artifactId>
            <version>${grpc_version}</version>
        </dependency>


## Add following extension:
        <extensions>
            <extension>
                <groupId>kr.motd.maven</groupId>
                <artifactId>os-maven-plugin</artifactId>
                <version>1.7.0</version>
            </extension>
        </extensions>

## Add following plugins :

        <plugin>
            <groupId>org.xolstice.maven.plugins</groupId>
            <artifactId>protobuf-maven-plugin</artifactId>
            <version>0.6.1</version>
            <configuration>
                <protocArtifact>com.google.protobuf:protoc:${protobuf_version}:exe:${os.detected.classifier}</protocArtifact>
            </configuration>
            <executions>
                <execution>
                    <goals><goal>compile</goal></goals>
                </execution>
                <execution>
                    <id>grpc-java</id>
                    <goals><goal>compile-custom</goal></goals>
                    <configuration>
                        <pluginId>grpc-java</pluginId>
                        <pluginArtifact>io.grpc:protoc-gen-grpc-java:${grpc_version}:exe:${os.detected.classifier}</pluginArtifact>
                    </configuration>
                </execution>
                <execution>
                    <id>grpc-kotlin</id>
                    <goals><goal>compile-custom</goal></goals>
                    <configuration>
                        <pluginId>grpc-kotlin</pluginId>
                        <pluginArtifact>io.rouz:grpc-kotlin-gen:${grpc-kotlin.version}:exe:${os.detected.classifier}</pluginArtifact>
                    </configuration>
                </execution>
            </executions>
        </plugin>

        <plugin>
            <artifactId>kotlin-maven-plugin</artifactId>
            <groupId>org.jetbrains.kotlin</groupId>
            <version>${kotlin.version}</version>
            <configuration>
                <jvmTarget>11</jvmTarget>
                <args>
                    <arg>-opt-in=kotlin.RequiresOptIn</arg>
                </args>
            </configuration>
            <executions>
                <execution>
                    <id>compile</id>
                    <phase>compile</phase>
                    <goals>
                        <goal>compile</goal>
                    </goals>
                    <configuration>
                        <sourceDirs>
                            <sourceDir>${project.basedir}/src/main/kotlin</sourceDir>
                            <sourceDir>${project.basedir}/target/generated-sources/protobuf/grpc-kotlin</sourceDir>
                            <sourceDir>${project.basedir}/target/generated-sources/protobuf/grpc-java</sourceDir>
                            <sourceDir>${project.basedir}/target/generated-sources/protobuf/java</sourceDir>
                        </sourceDirs>
                    </configuration>
                </execution>
                <execution>
                    <id>test-compile</id>
                    <phase>test-compile</phase>
                    <goals>
                        <goal>test-compile</goal>
                    </goals>
                </execution>
            </executions>

        </plugin>

## And the run
    mvn clean compile
